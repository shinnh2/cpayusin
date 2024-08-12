import iconStar from "./../assets/icon_star.svg";
import axios from "axios";
import { useState, useEffect } from "react";

const NavInfo = () => {
	const api = process.env.REACT_APP_API_URL;
	const [userRank, setUserRank] = useState<any[]>(["-", "-", "-"]);
	const [visitorInfo, setVisitorInfo] = useState({
		yesterday: 0,
		today: 0,
		total: 0,
	});
	useEffect(() => {
		axios
			.get(`${api}/api/v1/member/score`)
			.then((response) => {
				const length = response.data.data.length;
				const newRank = userRank;
				for (let i = 0; i < length; i++) {
					newRank[i] = response.data.data[i];
				}
				setUserRank(newRank);
			})
			.catch((error) => {
				console.error("에러", error);
			});

		axios
			.get(`${api}/api/v1/visitor`)
			.then((response) => {
				setVisitorInfo(response.data.data);
			})
			.catch((error) => {
				console.error("에러", error);
			});
	}, []);
	return (
		<div className="nav_info_wrap">
			<div className="statistics">
				<dl className="info all">
					<dt className="info_title">전체 방문자</dt>
					<dd className="info_content">{visitorInfo.total}</dd>
				</dl>
				<dl className="info">
					<dt className="info_title">어제</dt>
					<dd className="info_content">{visitorInfo.yesterday}</dd>
				</dl>
				<dl className="info">
					<dt className="info_title">오늘</dt>
					<dd className="info_content">{visitorInfo.today}</dd>
				</dl>
			</div>
			{/* <div className="user_rank">
				<p className="title">이달의 질문왕</p>
				<ul>
					{userRank.map((el, idx) => (
						<li className="user_rank_item" key={idx}>
							<img src={iconStar} alt="별 아이콘" className="icon" />
							<span className="grade">{`${idx + 1}등`}</span>
							<span className="user">{el === "-" ? el : el.nickname}</span>
						</li>
					))}
				</ul>
			</div> */}
		</div>
	);
};
export default NavInfo;
