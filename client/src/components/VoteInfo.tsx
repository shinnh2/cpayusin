import axios from "axios";
import { getAccessToken } from "../assets/tokenActions";
import { useState } from "react";

const VoteInfo = ({
	voteCount,
	isVoted,
	endpoint,
}: {
	voteCount: number | string;
	isVoted: boolean;
	endpoint: string;
}) => {
	const api = process.env.REACT_APP_API_URL;
	const [vetes, setVotes] = useState(voteCount);
	const [hasVoted, setHasVoted] = useState(isVoted);

	const handleClickVote = () => {
		const accessToken = getAccessToken();
		axios
			.post(
				`${api}${endpoint}`,
				{},
				{
					headers: { Authorization: accessToken },
				}
			)
			.then((_) => {
				alert("투표 성공");
				setVotes((prev) => Number(prev) + 1);
				setHasVoted(true);
			})
			.catch((_) => {
				alert("투표에 실패했습니다.");
			});
	};

	return (
		<button
			className={`info_item votes_info${hasVoted ? " voted" : ""}`}
			onClick={handleClickVote}
		>
			<span className="icon">득표수</span>
			<span className="info">{vetes}</span>
		</button>
	);
};
export default VoteInfo;
