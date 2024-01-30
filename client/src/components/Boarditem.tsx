//api문서 기준
// interface Data {
// 	postId: number;
// 	boardId: number;
// 	boardName: string;
// 	categoryId?: number;
// 	categoryName?: string;
// 	memberId: number;
// 	memberName: string;
// 	title: string;
// 	content: string;
// 	commentsCount?: number;
// 	createdAt: string;
// 	timeInfo: string;
// }

//서버 응답 기준
interface BoardItemData {
	postId: number;
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
	timeInfo: string;
}

const BoardItem = ({ data }: { data: BoardItemData }) => {
	return (
		<div className="board_item">
			<a
				href={`/board/${data.boardId}-${data.boardName}/${data.postId}`}
				title={data.title}
			>
				<div className="board_item_element_wrap">
					{data.categoryId ? (
						<p className="category_tag">{data.categoryName}</p>
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
						{/* <p className="info_item votes_info">
						<span className="icon">득표수</span>
						<span className="info">{data.voteCount}</span>
					</p> */}
					</div>
				</div>
				<h3 className="title">{data.title}</h3>
				<div className="board_item_element_wrap">
					<p className="board_date">{data.timeInfo}</p>
					<p className="board_writer">{data.memberName}</p>
				</div>
			</a>
		</div>
	);
};
export default BoardItem;
