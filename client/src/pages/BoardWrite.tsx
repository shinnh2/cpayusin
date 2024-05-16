import React, { ChangeEvent } from "react";
import SelectBox from "../components/SelectBox";
import Button from "../components/Button";
import EditorUnit from "../components/EditorUnit";
import Input from "../components/Input";
import axios from "axios";
import { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { getAccessToken } from "../assets/tokenActions";
// import boardData from "./../data/boardData.json";

interface FormType {
	title: string;
	content: string;
	boardId: string;
	// [key: string]: string; // 인덱스 시그니처 추가
}

interface BoardDataType {
	id: number;
	name: string;
	orderIndex: number;
	category: Array<any[]>;
}

const BoardWrite = () => {
	const api = process.env.REACT_APP_API_URL;
	const navigate = useNavigate();
	const [isAdmin, setIsAdmin] = useState(false);
	const [data, setData] = useState<any[]>([]); //전체 게시판 목록

	useEffect(() => {
		const accessToken = getAccessToken();
		//추후 로그인 실패했거나 안했을 경우 게시글 작성 막을 것!
		// if (!accessToken) {
		// 	alert("로그인이 필요한 서비스입니다.");
		// 	navigate(`/login`);
		// }
		//관리자 여부 판별
		axios
			.get(`${api}/api/v1/member/single-info`, {
				headers: { Authorization: accessToken },
			})
			.then((response) => {
				if (response.data.data.isAdmin) setIsAdmin(true);
			})
			.catch((_) => {});
		//게시판 정보 불러오기
		axios
			.get(`${api}/api/v1/board/menu`)
			.then((response) => {
				setData(response.data.data);
			})
			.catch((error) => {
				console.error("에러", error);
			});
	}, []);
	const [categoryItem, setCategoryItem] = useState<string[]>([]); //카테고리 목록
	const [nowBoardData, setNowBoardData] = useState<BoardDataType>(); //현재 선택된 게시판 정보
	const [nowCategoryId, setNowCategoryId] = useState(0); //현재 선택된 카테고리 id
	const [selectValueCategory, setSelectValueCategory] = useState(""); //현재 선택된 카테고리
	const [titleValue, setTitleValue] = useState("");

	const editorRef = useRef<any>(null); //작성된 내용
	//게시판 선택
	const handleSelectboard = (board: string) => {
		const matchedBoard = data.filter((el: any) => el.name === board)[0];
		setNowBoardData(matchedBoard);
		checkCategory(matchedBoard);
	};
	//카테고리 유무 확인
	const checkCategory = (board: BoardDataType) => {
		setCategoryItem([]);
		if (board !== undefined) {
			if (board.category.length !== 0) {
				const categorysArray = board.category.map((el: any) => el.name);
				setCategoryItem(categorysArray);
			}
		}
	};
	//카테고리 선택
	const handleSelectCategory = (categoryItem: string) => {
		const matchedCategory: any = nowBoardData!.category.find(
			(el: any) => el.name === categoryItem
		);
		setNowCategoryId(matchedCategory.id);
		setSelectValueCategory(categoryItem);
	};
	//취소 클릭시 목록으로 이동
	const cancelClickHandler = () => {
		navigate(`/board/${nowBoardData!.id}-${nowBoardData!.name}`);
	};
	//제출
	const submitHandler = () => {
		const id = nowCategoryId
			? nowCategoryId.toString()
			: nowBoardData!.id.toString(); //게시판 id
		const editorData: string = editorRef.current!.getInstance().getHTML(); //작성된 데이터
		const form: FormType = {
			title: titleValue,
			content: editorData,
			boardId: id,
		};

		const formData = new FormData();
		formData.append(
			"data",
			new Blob([JSON.stringify(form)], {
				type: "application/json",
			})
		);
		const postAxiosConfig = {
			headers: {
				"Content-Type": "multipart/form-data", // FormData를 사용할 때 Content-Type을 변경
				"Authorization": `${localStorage.getItem("accessToken")}`,
			},
		};
		axios
			.post(`${api}/api/v1/post/create`, formData, postAxiosConfig)
			.then((_) => {
				navigate(`/board/${nowBoardData!.id}-${nowBoardData!.name}`);
			})
			.catch((error) => {
				alert("게시판 등록을 실패했습니다.");
				if (error.response) {
					// 서버 응답이 있을 경우 (에러 상태 코드가 반환된 경우)
					console.error("서버 응답 에러:", error.response.data);
					console.error("응답 상태 코드:", error.response.status);
					console.error("응답 헤더:", error.response.headers);
				} else if (error.request) {
					// 요청이 전혀 되지 않았을 경우
					console.error("요청 에러:", error.request);
				} else {
					// 설정에서 문제가 있어 요청이 전송되지 않은 경우
					console.error("Axios 설정 에러:", error.message);
				}
				console.error("에러 구성:", error.config);
			});
	};

	return (
		<>
			{data.length !== 0 ? (
				<div className="board_box board_write_box">
					<div className="board_write_head">
						<div className="board_select_wrap">
							<SelectBox
								isLabel={false}
								selectItem={data.map((el: any) => {
									if (!isAdmin && el.isAdminOnly) {
										return "";
									}
									return el.name;
								})}
								placeHolder="게시판 선택"
								setHandleStatus={handleSelectboard}
							/>
							{categoryItem.length !== 0 ? (
								<SelectBox
									isLabel={false}
									selectItem={categoryItem}
									placeHolder="카테고리 선택"
									setHandleStatus={handleSelectCategory}
								/>
							) : null}
						</div>
						<div className="board_input_title">
							<Input
								InputLabel="제목"
								isLabel={false}
								errorMsg="제목을 입력해 주세요."
								inputAttr={{
									type: "text",
									placeholder: "게시글 제목을 입력하세요",
								}}
								setInputValue={setTitleValue}
								inputValue={titleValue}
							/>
						</div>
					</div>
					<EditorUnit ref={editorRef} />
					<div className="board_editor_button_wrap">
						<Button
							buttonType="primary"
							buttonSize="big"
							buttonLabel="작성 완료"
							onClick={submitHandler}
						/>
						<Button
							buttonType="no_em"
							buttonSize="big"
							buttonLabel="취소"
							onClick={cancelClickHandler}
						/>
					</div>
				</div>
			) : (
				<div className="board_box board_write_box">
					생성된 게시판이 없습니다.
				</div>
			)}
		</>
	);
};
export default BoardWrite;
