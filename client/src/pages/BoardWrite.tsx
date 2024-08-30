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
	boardId: string | number;
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
			.get(`${api}/api/v1/member/profile`, {
				headers: { Authorization: accessToken },
			})
			.then((response) => {
				if (response.data.data.role === "ADMIN") setIsAdmin(true);
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
	const [titleValue, setTitleValue] = useState(""); //제목
	const [filesFormData, setFilesFormData] = useState<FormData>(new FormData()); //이미지파일

	const editorRef = useRef<any>(null); //작성된 내용

	//함수 - 게시판 선택
	const handleSelectboard = (board: string) => {
		const matchedBoard = data.filter((el: any) => el.name === board)[0];
		setNowBoardData(matchedBoard);
		checkCategory(matchedBoard);
	};

	//함수 - 카테고리 유무 확인
	const checkCategory = (board: BoardDataType) => {
		setCategoryItem([]);
		if (board !== undefined) {
			if (board.category.length !== 0) {
				const categorysArray = board.category.map((el: any) => el.name);
				setCategoryItem(categorysArray);
			}
		}
	};

	//함수 - 카테고리 선택
	const handleSelectCategory = (categoryItem: string) => {
		const matchedCategory: any = nowBoardData!.category.find(
			(el: any) => el.name === categoryItem
		);
		setNowCategoryId(matchedCategory.id);
		setSelectValueCategory(categoryItem);
	};
	//취소 클릭시 목록으로 이동
	const cancelClickHandler = () => {
		navigate(`/board/${nowBoardData!.name}`);
	};

	//이미지 관련

	//함수 - 이미지 blob데이터 저장
	const setBlobData = (blob: Blob) => {
		const newFormData = new FormData();
		// 기존 formData의 모든 항목을 newFormData로 복사
		filesFormData.forEach((value, key) => {
			newFormData.append(key, value);
		});
		newFormData.append("files", blob); // blob 데이터를 추가
		setFilesFormData(newFormData); // 업데이트된 FormData를 상태에 저장
	};

	//함수 - 이미지 전송: postId, imgsUrl 리턴
	const uploadImage = async (formData: FormData, config: any) => {
		try {
			const res = await axios.post(
				`${api}/api/v1/post/create`, //240828임시URL변경
				formData,
				config
			);
			const postId = res.data.data.id;
			const filesUrls = res.data.data.files;
			return [postId, filesUrls];
		} catch (error) {
			console.log("이미지 전송 실패", error);
			return ["", ""];
		}
	};

	//함수 - content에서 이미지 데이터 찾아서 변환(base64 -> url)
	const imgConverter = (content: string, urlArr: string[]): string => {
		const base64ImageRegex =
			/<img src="data:image\/(png|jpeg|jpg);base64,[^"]+"[^>]*>/g;
		const base64Images = content.match(base64ImageRegex) || [];

		// base64 이미지를 URL로 교체
		let processedContent = content;
		base64Images.forEach((base64ImageTag, index) => {
			const imageUrl = urlArr[index];
			const newImageTag = base64ImageTag.replace(
				/src="data:image\/(png|jpeg|jpg);base64,[^"]+"/,
				`src="${imageUrl}"`
			);
			processedContent = processedContent.replace(base64ImageTag, newImageTag);
		});

		return processedContent;
	};

	//함수 - 게시글 제출
	const submitHandler = async () => {
		//게시판 선택하지 않았을 경우
		if (nowBoardData === undefined) {
			alert("게시판을 선택해주세요.");
			return;
		}
		//게시판 id
		const id = nowCategoryId
			? nowCategoryId.toString()
			: nowBoardData!.id.toString();
		//작성된 데이터
		const editorData: string = editorRef.current!.getInstance().getHTML();
		//데이터 전송시 설정값, 토큰
		const postAxiosConfig = {
			headers: {
				"Content-Type": "multipart/form-data", // FormData를 사용할 때 Content-Type을 변경
				Authorization: `${getAccessToken()}`,
			},
		};

		//이미지 없는 경우
		if (filesFormData.get("files") === null) {
			const postData = {
				title: titleValue,
				content: editorData,
				boardId: id,
			};
			const newFormData: FormData = new FormData();
			newFormData.append(
				"data",
				new Blob([JSON.stringify(postData)], {
					type: "application/json",
				})
			);
			axios
				.post(`${api}/api/v1/post/create`, newFormData, postAxiosConfig) //240828임시URL변경
				.then((response) => {
					const newPostId = response.data.data.id;
					navigate(`/${newPostId}`);
				})
				.catch((error) => {
					alert("게시판 등록을 실패했습니다.");
				});
		} else {
			//이미지 있는 경우
			let postData = {
				title: "images",
				content: "",
				boardId: id,
			};
			const newFormData: FormData = new FormData();
			filesFormData.forEach((value, key) => {
				newFormData.append(key, value);
			});
			newFormData.append(
				"data",
				new Blob([JSON.stringify(postData)], {
					type: "application/json",
				})
			);
			//1. 이미지 먼저 전송: postId, imgsUrl값 반환
			const ImagesReponse = await uploadImage(newFormData, postAxiosConfig);
			const [postId, filesUrls] = ImagesReponse;
			if (postId === "" || filesUrls === "") {
				console.log("이미지 전송 실패");
				return;
			}
			console.log(postId, filesUrls);

			//2. 이미지 src를 base64 -> url로 변경
			const newContent = imgConverter(editorData, filesUrls);

			//3. 이미지 최종 전송(이미지 등록시 이미 게시글 생성되었으므로 게시글 수정으로 제출)
			postData.title = titleValue;
			postData.content = newContent;
			newFormData.set(
				"data",
				new Blob([JSON.stringify(postData)], {
					type: "application/json",
				})
			);

			axios
				.patch(
					`${api}/api/v1/post/update/${postId}`, //240828임시URL변경
					newFormData,
					postAxiosConfig
				)
				.then((_) => {
					navigate(`/board/${nowBoardData!.name}`);
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
		}
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
								maxLength={50}
							/>
						</div>
					</div>
					<EditorUnit ref={editorRef} setBlobData={setBlobData} />
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
