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
		// //카테고리 목록 받아오기: 삭제
		// axios
		// 	.get(`${api}/api/v1/category/${boardId}`)
		// 	.then((response) => {
		// 		setCategoryItem(response.data.data);
		// 	})
		// 	.catch((error) => {
		// 		console.error("에러", error);
		// 	});
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
		const form: FormType = {
			title: titleValue,
			content: editorData,
		};

		const postAxiosConfig = {
			headers: {
				Authorization: `${getAccessToken()}`,
			},
		};
		axios
			.patch(`${api}/api/v1/post/${params.postId}`, form, postAxiosConfig)
			.then((_) => {
				navigate(`/${params.postId}`);
			})
			.catch((error) => {
				console.log(error);
				alert("게시글 등록에 실패했습니다.");
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
