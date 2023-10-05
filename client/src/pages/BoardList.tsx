import BoardItem from "../components/Boarditem";

const dummyData = {
	id: 53,
	title: "JPA란",
	content: "~~~~~~",
	files: [
		{
			originalFileName: "orca.jfif",
			storedFileName: "ddfd5ebb-c490-4b9c-bdc0-75c2ccc9e437.jfif",
			url: "https://jbaccount.s3.ap-northeast-2.amazonaws.com/post/ddfd5ebb-c490-4b9c-bdc0-75c2ccc9e437.jfif",
		},
	],
	voteCount: 0,
	voteStatus: false,
	member: {
		id: 1,
		nickname: "운영자",
	},
	createdAt: "2023-09-07T18:48:14.6994829",
	modifiedAt: "2023-09-07T18:48:14.6994829",
};

const BoardList = () => {
	return (
		<div>
			<h3>게시판 이름</h3>
			<ul className="board_box board_list_box">
				<li>
					<BoardItem data={dummyData} />
				</li>
				<li>
					<BoardItem data={dummyData} />
				</li>
				<li>
					<BoardItem data={dummyData} />
				</li>
			</ul>
		</div>
	);
};
export default BoardList;
