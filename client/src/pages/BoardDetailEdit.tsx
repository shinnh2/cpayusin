import React, { ChangeEvent } from "react";
import SelectBox from "../components/SelectBox";
import Button from "../components/Button";
import EditorUnit from "../components/EditorUnit";
import Input from "../components/Input";
import axios from "axios";
import { useEffect, useState, useRef } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getAccessToken } from "../assets/tokenActions";

interface FormType {
	title: string;
	content: string;
	categoryId?: number | string;
}

interface categoryDataType {
	id: number;
	name: string;
	orderIndex: number;
	adminOnly: boolean;
}
interface postData {
	memberId: number;
	boardId: number;
	categoryId: number;
	postId: number;
	nickname: string;
	title: string;
	content: string;
	files: any[];
	voteCount: number;
	voteStatus: boolean;
	createdAt: string;
}

const BoardDetailEdit = () => {
	const api = process.env.REACT_APP_API_URL;
	const [accessToken, setAccessToken] = useState("");
	const navigate = useNavigate();
	const [postdata, setPostData] = useState<postData>();
	const params = useParams();
	const boardId = params.postId;
	const [categoryItem, setCategoryItem] = useState<categoryDataType[]>([]); //카테고리 목록
	const [nowCategoryId, setNowCategoryId] = useState(0); //현재 선택된 카테고리 id
	const [selectValueCategory, setSelectValueCategory] = useState(""); //현재 선택된 카테고리
	const [titleValue, setTitleValue] = useState("");
	const editorRef = useRef<any>(null); //작성된 내용
	useEffect(() => {
		//게시글 정보 받아오기
		axios
			.get(`${api}/api/v1/post/${params.postId}`)
			.then((response) => {
				const data = response.data.data;
				setPostData(data);
				setTitleValue(data.title);
				editorRef.current?.getInstance().setHTML(data.content);
			})
			.catch((error) => {
				console.error("에러", error);
			});
		//카테고리 목록 받아오기
		axios
			.get(`${api}/api/v1/category/${boardId}`)
			.then((response) => {
				setCategoryItem(response.data.data);
			})
			.catch((error) => {
				console.error("에러", error);
			});
	}, []);
	//카테고리 선택
	const handleSelectCategory = (categoryName: string) => {
		const matchedCategory: categoryDataType | undefined = categoryItem!.find(
			(el: categoryDataType) => el.name === categoryName
		);
		setNowCategoryId(matchedCategory!.id);
		setSelectValueCategory(categoryName);
	};
	//취소 클릭시 게시글로 이동
	const cancelClickHandler = () => {
		navigate(`/${params.postId}`);
	};
	//제출
	const submitHandler = () => {
		const editorData: string = editorRef.current!.getInstance().getHTML(); //작성된 데이터
		const form: FormType =
			nowCategoryId > 0
				? {
						title: titleValue,
						content: editorData,
						categoryId: nowCategoryId,
				  }
				: {
						title: titleValue,
						content: editorData,
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
				Authorization: `${getAccessToken()}`,
			},
		};
		axios
			.patch(
				`http://13.124.241.118:8080/api/v1/post/update/${params.postId}`, //240828임시URL변경
				formData,
				postAxiosConfig
			)
			.then((_) => {
				navigate(`/${params.postId}`);
			})
			.catch((error) => {
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
		<div className="board_box board_write_box">
			<div className="board_write_head">
				<div className="board_select_wrap">
					{categoryItem.length !== 0 ? (
						<SelectBox
							isLabel={false}
							selectItem={categoryItem.map((el: categoryDataType) => el.name)}
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
					buttonLabel="수정 완료"
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
	);
};
export default BoardDetailEdit;
