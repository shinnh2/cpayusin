const CommentItem = () => {
	return (
		<div className="board_item">
			<div className="board_item_element_wrap">
				<a href="" className="link em">
					게시글 바로가기
				</a>
				<div className="board_info">
					<p className="info_item votes_info">
						<span className="icon">득표수</span>
						<span className="info">12</span>
					</p>
				</div>
			</div>
			<p className="comment">
				작성한 댓글입니다. 이런 내용이 달렸어요 만약 2줄이 넘어가면 ... 으로
				처리되지요 ...작성한 댓글입니다. 이런 내용이 달렸어요 만약 2줄이
				넘어가면 ... 으로 처리되지요 ...
			</p>
			<div className="board_item_element_wrap">
				<p className="board_date">2023.9.30</p>
			</div>
		</div>
	);
};
export default CommentItem;
