import { useEffect } from "react";

interface BoardItemData {
	postId?: number;
	boardId: number;
	boardName: string;
	categoryId?: number;
	categoryName?: string;
	memberId: number;
	memberName: string;
	commentsCount: number;
	title: string;
	content: string;
	createdAt: string;
}

const BoardItem = ({ data }: { data: BoardItemData }) => {
	//날짜 변경 함수
	const changedDate = (date: string) => {
		const newDate = new Date(date);
		return newDate.toLocaleString("ko-KR");
	};

	return (
		<div className="board_item">
			<a href={`/${data.postId}`} title={data.title}>
				<div className="board_item_element_wrap">
					{data.boardId ? (
						<p className="category_tag">{data.boardName}</p>
					) : (
						<p className="category_tag no_category">카테고리없음</p>
					)}
					<div className="board_info">
						{data.commentsCount ? (
							<p className="info_item comments_info">
								<span className="icon">댓글수</span>
								<span className="info">{data.commentsCount}</span>
							</p>
						) : null}
					</div>
				</div>
				<h3 className="title">{data.title}</h3>
				<div className="board_item_element_wrap">
					<p className="board_date">{changedDate(data.createdAt)}</p>
					<p className="board_writer">{data.memberName}</p>
				</div>
			</a>
		</div>
	);
};
export default BoardItem;
