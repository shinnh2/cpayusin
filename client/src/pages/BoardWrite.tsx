import SelectBox from "../components/SelectBox";
import Button from "../components/Button";
import EditorUnit from "../components/EditorUnit";
import boardData from "./../data/boardData.json";
import { useEffect, useState, useRef, ReactElement } from "react";

const BoardWrite = () => {
	const data = boardData;
	const [selectItemBoard, setSeletItemBoard] = useState(
		data.map((el: any) => el.name)
	);
	const [selectValueBoard, setSelectValueBoard] = useState("");
	const [selectItemCategory, setSeletItemCategory] = useState<string[]>([]);
	const [selectValueCategory, setSelectValueCategory] = useState("");
	const [writtenData, setWrittenData] = useState("");
	const handleSelectboard = (board: string) => {
		setSelectValueBoard(board);
		checkCategory(board);
	};
	const checkCategory = (board: string) => {
		const matchedBoard = data.filter((el: any) => el.name === board)[0];
		setSeletItemCategory([]);
		if (matchedBoard !== undefined) {
			if (matchedBoard.categories.length !== 0) {
				const categorysArray = matchedBoard.categories.map(
					(el: any) => el.categoryName
				);
				setSeletItemCategory(categorysArray);
			}
		}
	};
	const handleSelectCategory = (category: string) => {
		setSelectValueCategory(category);
	};
	const editorRef = useRef<any>(null);

	const submitHandler = () => {
		const editorData: string = editorRef.current!.getInstance().getHTML();
		console.log(editorData);
		// setBoardData(data);
	};
	const handleWriteComplete = () => {
		console.log(boardData);
	};

	return (
		<div className="board_box board_write_box">
			<div className="board_write_head">
				<div className="board_select_wrap">
					<SelectBox
						isLabel={false}
						selectItem={selectItemBoard}
						placeHolder="게시판 선택"
						setHandleStatus={handleSelectboard}
					/>
					{selectItemCategory.length !== 0 ? (
						<SelectBox
							isLabel={false}
							selectItem={selectItemCategory}
							placeHolder="카테고리 선택"
							setHandleStatus={handleSelectCategory}
						/>
					) : null}
				</div>
				<div className="board_input_title">
					<input
						type="text"
						placeholder="게시글 제목을 입력하세요"
						title="게시글 제목"
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
				<Button buttonType="no_em" buttonSize="big" buttonLabel="취소" />
			</div>
		</div>
	);
};
export default BoardWrite;
