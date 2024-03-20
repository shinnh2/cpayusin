import { ChangeEvent, useEffect, useState } from "react";
import Input from "../components/Input";
import Button from "../components/Button";
import IconAdd from "../assets/icon_add.svg";
import boardData from "./../data/boardData.json";
import React from "react";
import { DragDropContext } from "react-beautiful-dnd";
import { StrictModeDroppable as Droppable } from "../components/StrictModeDroppable";
import AdminBoardItem from "../components/AdminBoardItem";

// interface DragTarget {
// 	dragOverHandler(event: DragEvent): void;
// 	dropHandler(event: DragEvent): void;
// 	dragLeaveHandler(event: DragEvent): void;
// }
// interface Draggable {
// 	dragStartHandler(event: DragEvent, id: number): void;
// 	dragEndHandler(event: DragEvent): void;
// }

//보드 생성
const InnerList = React.memo(function InnerList({
	board,
	categories,
	index,
	editCategory,
	editData,
}: any) {
	return (
		<AdminBoardItem
			board={board}
			categories={categories}
			index={index}
			editCategory={editCategory}
			editData={editData}
		/>
	);
});

const Admin = () => {
	//데이터 맵 생성
	const boardMap: any = { boards: {} };
	boardMap.boardsOrder = [];
	boardData.forEach((el) => {
		boardMap.boards[`board-${el.id}`] = el;
		boardMap.boardsOrder.push(`board-${el.id}`);
	});

	const [boardListData, setBoardListData] = useState(boardMap);
	const [newBoard, setNewBoard] = useState("");
	const [isAdminOnlyBoard, setIsAdminOnlyBoard] = useState<boolean>(false);

	//게시판 생성하기
	const handleClickCreateBoard = () => {
		console.log(newBoard, isAdminOnlyBoard);
	};
	const setInputValueNewBoard = (value: string) => {
		setNewBoard(value);
	};
	const handleSetIsAdminOnlyCheck = (event: ChangeEvent<HTMLInputElement>) => {
		if (event.target.checked) setIsAdminOnlyBoard(true);
		else setIsAdminOnlyBoard(false);
	};

	//보드명 및 카테고리명 수정하기
	const editData = (
		dataType: "board" | "category",
		boardId: any,
		categoryIndex: any,
		newName: string
	) => {
		const newBoard = boardListData.boards[`board-${boardId}`];
		if (dataType === "board") {
			newBoard.name = newName;
		}
		if (dataType === "category") {
			newBoard.categories[categoryIndex].categoryName = newName;
		}
		setBoardListData({
			...boardListData,
			boards: {
				...boardListData.boards,
				[`board-${boardId}`]: newBoard,
			},
		});
	};

	const handlerOnDragEnd = (result: any) => {
		const { destination, source, draggableId, type } = result;

		// console.log("source", source); //드래그 되기 전 위치: ex) {index: 0, droppableId: 'column1'}
		// console.log("destination", destination); //드래그 된 후 위치: ex) {droppableId: 'column2', index: 0}
		// console.log("draggableId", draggableId); //드래그 대상의 아이디: ex) draggableId task1
		// console.log("type", type); //드래그 대상의 타입: ex) type task

		//드래그 된 곳이 없을 경우
		if (!destination) {
			return;
		}

		//드래그 하기 전 위치와 드래그 된 후의 위치가 같을 경우
		if (
			destination.droppableId === source.droppableId &&
			destination.index === source.index
		) {
			return;
		}

		//보드 이동 처리
		if (type === "board") {
			const newBoardOrder = Array.from(boardListData.boardsOrder);
			newBoardOrder.splice(source.index, 1);
			newBoardOrder.splice(destination.index, 0, draggableId);

			setBoardListData({
				...boardListData,
				boardsOrder: newBoardOrder,
			});
			return;
		}

		//카테고리 이동 출발, 도착 지점 설정
		const start = boardListData.boards[source.droppableId]; //드래그 되기 전 위치(열)
		const finish = boardListData.boards[destination.droppableId]; //드래그 되기 후 위치(열)

		//시작지점에서 카테고리 정보 추출
		const draggedCategoryId = draggableId.split("-")[1]; //드래그된 카테고리 아이디 "1"
		const startCategorys = Array.from(start.categories); //시작지점의 카테고리
		//(시작지점에서 추출한) 드래그된 카테고리의 정보
		const draggedCategory = startCategorys.filter(
			(el: any) => el.id === +draggedCategoryId
		)[0]; //이게 오래 걸리면... 카테고리도 맵 생각해봐야 할듯

		//보드 내 카테고리 이동 처리
		if (start === finish) {
			startCategorys.splice(source.index, 1);
			startCategorys.splice(destination.index, 0, draggedCategory);

			const newStartBoard = {
				...start,
				categories: startCategorys,
			};

			setBoardListData({
				...boardListData,
				boards: {
					...boardListData.boards,
					[`board-${newStartBoard.id}`]: newStartBoard,
				},
			});
			return;
		}

		//나머지 (보드 외 카테고리 이동) 처리
		startCategorys.splice(source.index, 1);
		const newStartBoard = {
			...start,
			categories: startCategorys,
		};

		const finishCategorys = Array.from(finish.categories);
		finishCategorys.splice(destination.index, 0, draggedCategory);
		const newFinishBoard = {
			...finish,
			categories: finishCategorys,
		};

		setBoardListData({
			...boardListData,
			boards: {
				...boardListData.boards,
				[`board-${newStartBoard.id}`]: newStartBoard,
				[`board-${newFinishBoard.id}`]: newFinishBoard,
			},
		});
	};

	//최종 데이터 변형
	const handleClickSubmit = () => {
		const boardDataArray: any[] = [];
		let boardOrderIndex = 0;
		/* 
		1. boardDataArray=[], boardOrderIndex=0 생성
		2. boardOrder의 각 아이템 boardId를 순회하며
			2.1. isDeleted 확인 및 board의 orderIndex 수정
			- nowBoard=boards[boardId]로 찾기
			- nowBoard.isDeleted가 true가 아닌 경우: nowBoard.orderIndex=boardOrderIndex+=1
			2.2. category의 orderIndex 수정
			- categoryOrderIndex=0 생성
			- nowBoard.categories의 각 아이템 category를 순회하며
			category.isDeleted가 없거나 false인 경우: category.orderIndex=categoryOrderIndex+=1
			2.3. boardDataArray에 nowBoard를 push
		*/
		for (let boardId of boardListData.boardsOrder) {
			const nowBoard = boardListData.boards[boardId];
			if (!nowBoard.isDeleted) {
				boardOrderIndex = boardOrderIndex + 1;
				nowBoard.orderIndex = boardOrderIndex;
			}
			if (nowBoard.categories.length > 0) {
				let categoryOrderIndex = 0;
				for (let category of nowBoard.categories) {
					if (!category.isDeleted) {
						categoryOrderIndex += 1;
						category.orderIndex = categoryOrderIndex;
					}
				}
			}
			boardDataArray.push(nowBoard);
		}

		console.log(boardDataArray);
	};

	useEffect(() => {}, []);

	return (
		<div className="admin_wrap">
			<h3 className="admin_title">관리자 페이지</h3>
			<p className="admin_board_desc">
				드래그 앤 드롭으로 게시판 및 카테고리 순서를 변경할 수 있습니다.
			</p>

			<DragDropContext onDragEnd={handlerOnDragEnd}>
				<Droppable droppableId="all-boards" direction="vertical" type="board">
					{(provided) => (
						<div
							className="admin_board_list_wrap"
							ref={provided.innerRef}
							{...provided.droppableProps}
						>
							{boardListData.boardsOrder.map((boardId: any, index: any) => {
								return (
									<InnerList
										key={boardId}
										board={boardListData.boards[boardId]}
										categories={boardListData.boards[boardId].categories}
										index={index}
										editData={editData}
									/>
								);
							})}
							{provided.placeholder}
						</div>
					)}
				</Droppable>
			</DragDropContext>

			<div className="admin_board_create_wrap">
				<h4 className="title">새 게시판 추가하기</h4>
				<Input
					InputLabel="새 게시판 이름"
					isLabel={false}
					inputAttr={{
						type: "text",
						placeholder: "새 게시판 이름을 입력하세요",
					}}
					setInputValue={setInputValueNewBoard}
					inputValue={newBoard}
				>
					<button className="add_board_btn" onClick={handleClickCreateBoard}>
						<img src={IconAdd} alt="게시판 추가 버튼 아이콘" />
					</button>
				</Input>
				<div className="isAdminOnly_check_wrap">
					<input
						type="checkbox"
						id="isAdminOnlyCheck"
						onChange={(e) => handleSetIsAdminOnlyCheck(e)}
					/>
					<label htmlFor="isAdminOnlyCheck">관리자만 작성 가능</label>
				</div>
			</div>

			<div className="admin_button_wrap">
				<Button
					buttonType="primary"
					buttonSize="big"
					buttonLabel="게시글 수정 완료"
					onClick={handleClickSubmit}
				/>
				<Button buttonType="no_em" buttonSize="big" buttonLabel="취소" />
			</div>
		</div>
	);
};
export default Admin;
