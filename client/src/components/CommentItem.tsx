interface CommentItemData {
	postId: number;
	postTitle: string;
	commentId: number;
	text: string;
	voteCount: number;
	voteStatus: boolean;
	createdAt?: string;
}

const CommentItem = ({ data }: { data: any }) => {
	return (
		<div className="board_item">
			<div className="board_item_element_wrap">
				<a
					href={`/board/${data.boardId}-${data.boardName}/${data.postId}`}
					className="link em"
				>
					게시글 바로가기
				</a>
				<div className="board_info">
					<p className="info_item votes_info">
						<span className="icon">득표수</span>
						<span className="info">{data.voteCount}</span>
					</p>
				</div>
			</div>
			<p className="comment">{data.text}</p>
			<div className="board_item_element_wrap">
				<p className="board_date">{data.createdAt}</p>
			</div>
		</div>
	);
};
export default CommentItem;
