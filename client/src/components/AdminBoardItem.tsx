import React from "react";
import { useState, useEffect } from "react";
import Input from "./Input";
import Button from "./Button";
import IconAdd from "../assets/icon_add.svg";
import { ReactComponent as IconArrowDown } from "./../assets/icon_arrow_down.svg";
import { Draggable } from "react-beautiful-dnd";
import { StrictModeDroppable as Droppable } from "./StrictModeDroppable";
import AdminCategoryItem from "./AdminCategoryItem";
import axios from "axios";
import { getAccessToken } from "../assets/tokenActions";

const InnerList = React.memo(function InnerList({
	board,
	category,
	editData,
}: any) {
	return category.map((categoryItem: any, index: any) => (
		<AdminCategoryItem
			key={categoryItem.id}
			category={categoryItem}
			index={index}
			board={board}
			editData={editData}
		/>
	));
});

function AdminBoardItem({
	board,
	category,
	index,
	editData,
	fetchData,
	fetchMenuData,
}: any) {
	const api = process.env.REACT_APP_API_URL;
	const [newCategory, setNewCategory] = useState("");
	const [isEdit, setIsEdit] = useState(false);
	const [boardName, setBoardName] = useState(board.name);
	const [isDelete, setIsDelete] = useState(
		"isDeleted" in board ? board["isDeleted"] : false
	);
	const setInputValueNewCategory = (value: string) => {
		setNewCategory(value);
	};
	//카테고리 생성
	const handleClickCreateCategory = () => {
		const newCategoryData = {
			name: newCategory,
			isAdminOnly: false,
			parentId: board.id,
		};
		const postAxiosConfig = {
			headers: {
				"Content-Type": "application/json",
				Authorization: `${getAccessToken()}`,
			},
		};
		axios
			.post(
				`${api}/api/v1/admin/manage/board/create`,
				newCategoryData,
				postAxiosConfig
			)
			.then((response) => {
				alert("카테고리가 추가되었습니다.");
				fetchData();
			})
			.catch((error) => {
				console.error("에러", error);
			});
	};
	const clickHandler = (event: React.MouseEvent<HTMLElement>): void => {
		event.currentTarget.parentElement!.parentElement!.classList.toggle("fold");
	};

	//이름 수정
	const handleSetBoardName = (value: string) => {
		setBoardName(value);
	};
	const handleBoardEdit = () => {
		editData("board", board.id, -1, boardName);
		setIsEdit(false);
	};
	//삭제
	const handleBoardDelete = () => {
		const isBoardDelete = window.confirm(
			"게시판 안의 모든 카테고리와 게시글이 삭제됩니다. 그래도 진행하시겠습니까?"
		);
		if (isBoardDelete) {
			board["isDeleted"] = true;
			setIsDelete(true);
			alert(
				"게시판을 삭제했습니다. 게시글 수정완료를 클릭하셔야 최종 반영됩니다."
			);
		} else {
			alert("게시판 삭제를 취소했습니다.");
		}
	};
	return (
		<Draggable draggableId={`board-${board.id}`} index={index}>
			{(provided) => (
				<div
					className={`admin_board_list_item ${isDelete ? "deleted" : ""}`}
					ref={provided.innerRef}
					{...provided.draggableProps}
				>
					<div className="admin_board_list_head" {...provided.dragHandleProps}>
						<div className="list_name">
							{isEdit ? (
								<Input
									InputLabel="게시판명 수정"
									isLabel={false}
									inputAttr={{
										type: "text",
										placeholder: "게시판명을 입력하세요",
									}}
									setInputValue={handleSetBoardName}
									inputValue={boardName}
								/>
							) : (
								board.name
							)}
						</div>
						{isEdit ? (
							<div className="list_button_wrap editing">
								<Button
									buttonType="another"
									buttonSize="small"
									buttonLabel="취소"
									onClick={() => setIsEdit(false)}
								/>
								<Button
									buttonType="another"
									buttonSize="small"
									buttonLabel="수정완료"
									onClick={handleBoardEdit}
								/>
							</div>
						) : (
							<div className="list_button_wrap">
								<Button
									buttonType="another"
									buttonSize="small"
									buttonLabel="수정"
									onClick={() => setIsEdit(true)}
								/>
								<Button
									buttonType="another"
									buttonSize="small"
									buttonLabel="삭제"
									onClick={handleBoardDelete}
								/>
							</div>
						)}
						<button onClick={clickHandler} className="admin_board_icon">
							메뉴 펼치기 접기 토글 아이콘
							<IconArrowDown />
						</button>
					</div>

					<Droppable droppableId={`board-${board.id}`} type="category">
						{(provided, snapshot) => (
							<div
								className={
									snapshot.isDraggingOver
										? "admin_category_list draggingOver"
										: "admin_category_list"
								}
								ref={provided.innerRef}
								{...provided.droppableProps}
							>
								<InnerList
									board={board}
									category={category}
									editData={editData}
								/>
								{provided.placeholder}
							</div>
						)}
					</Droppable>
					<div className="admin_board_create_wrap">
						<h4 className="title">새 카테고리 추가하기</h4>
						<Input
							InputLabel="새 카테고리 이름"
							isLabel={false}
							inputAttr={{
								type: "text",
								placeholder: "새 카테고리 이름을 입력하세요",
							}}
							setInputValue={setInputValueNewCategory}
							inputValue={newCategory}
						>
							<button className="add_board_btn">
								<img
									src={IconAdd}
									alt="카테고리 추가 버튼 아이콘"
									onClick={handleClickCreateCategory}
								/>
							</button>
						</Input>
					</div>
				</div>
			)}
		</Draggable>
	);
}

export default AdminBoardItem;
