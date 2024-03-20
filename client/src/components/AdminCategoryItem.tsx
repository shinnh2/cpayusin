import { Draggable } from "react-beautiful-dnd";
import IconDraggable from "./../assets/icon_sort.svg";
import Button from "./Button";
import Input from "./Input";
import { useState } from "react";

function AdminCategoryItem({ category, index, board, editData }: any) {
	const [isEdit, setIsEdit] = useState(false);
	const [categoryName, setCategoryName] = useState(category.categoryName);
	const [isDelete, setIsDelete] = useState(
		"isDeleted" in category ? category["isDeleted"] : false
	);
	//이름 수정
	const handleSetCategoryName = (value: string) => {
		setCategoryName(value);
	};
	const handleCategoryEdit = () => {
		editData("category", board.id, index, categoryName);
		setIsEdit(false);
	};
	//삭제
	const handleCategoryDelete = () => {
		category["isDeleted"] = true;
		setIsDelete(true);
	};
	return (
		<Draggable draggableId={`category-${category.id}`} index={index}>
			{(provided, snapshot) => (
				<div
					className={
						snapshot.isDragging
							? "category_list_item dragging"
							: `category_list_item ${isDelete ? "deleted" : ""}`
					}
					ref={provided.innerRef}
					{...provided.draggableProps}
					{...provided.dragHandleProps}
				>
					<div className="draggable_icon">
						<img src={IconDraggable} alt="순서 편집 아이콘" />
					</div>
					<div className="list_name">
						{isEdit ? (
							<Input
								InputLabel="카테고리명 수정"
								isLabel={false}
								inputAttr={{
									type: "text",
									placeholder: "카테고리명을 입력하세요",
								}}
								setInputValue={handleSetCategoryName}
								inputValue={categoryName}
							/>
						) : (
							category.categoryName
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
								onClick={handleCategoryEdit}
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
								onClick={handleCategoryDelete}
							/>
						</div>
					)}
				</div>
			)}
		</Draggable>
	);
}

export default AdminCategoryItem;
