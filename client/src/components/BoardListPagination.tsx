import { useEffect, useState, Dispatch } from "react";
import BoardItem from "../components/Boarditem";
import { useParams } from "react-router-dom";
import axios from "axios";
import { getAccessToken } from "../assets/tokenActions";

interface BoardListPaginationProps {
	url: string;
}

const BoardListPagination = () => {
	const [pageInfo, setPageInfo] = useState({
		totalPages: 1,
		maxPaginationValue: 5,
	});
	const [nowPage, setNowPage] = useState(1);
	const [pagenation, setPagenation] = useState(
		generatePageRange(1, pageInfo.totalPages)
	);
	const params = useParams();
	const api = process.env.REACT_APP_API_URL;
	const [data, setData] = useState<any[]>([]);
	useEffect(() => {
		const token = getAccessToken();
		axios
			.get(`${api}/api/v1/profile/my-posts?page=1&size=8`, {
				headers: { Authorization: token },
			}) //url
			.then((response) => {
				const pageInfo = response.data.pageInfo;
				const totalPageNum = pageInfo.totalPages;
				const maxPaginationNum = totalPageNum >= 5 ? 5 : totalPageNum;
				setPageInfo({
					totalPages: totalPageNum,
					maxPaginationValue: maxPaginationNum,
				});
				setPagenation(generatePageRange(1, totalPageNum));
				setData(response.data.data);
			})
			.catch((error) => {
				console.error("에러", error);
			});
	}, []);
	useEffect(() => {
		axios
			.get(`${api}/api/v1/profile/my-posts?page=${nowPage}&size=8`) //url
			.then((response) => {
				setData(response.data.data);
			})
			.catch((error) => {
				console.error("에러", error);
			});
	}, [nowPage]);

	//페이지네이션 범위 설정 함수
	function generatePageRange(start: number, end: number) {
		return Array.from({ length: end + 1 - start }, (_, i) => start + i);
	}

	const handlaClickPagination = (page: number) => {
		if (nowPage !== page) setNowPage(page);
	};
	//이전 클릭
	const handleClickPrev = () => {
		if (nowPage === 1) return;
		if (nowPage % pageInfo.maxPaginationValue === 1)
			setPagenation(
				generatePageRange(nowPage - pageInfo.maxPaginationValue, nowPage - 1)
			);
		setNowPage(nowPage - 1);
	};
	//다음 클릭
	const handleClickNext = () => {
		if (nowPage === pageInfo.totalPages) return;
		if (nowPage % pageInfo.maxPaginationValue === 0) {
			if (nowPage + pageInfo.maxPaginationValue >= pageInfo.totalPages)
				setPagenation(generatePageRange(nowPage + 1, pageInfo.totalPages));
			else
				setPagenation(
					generatePageRange(nowPage + 1, nowPage + pageInfo.maxPaginationValue)
				);
		}
		setNowPage(nowPage + 1);
	};

	return (
		<div>
			{data.length === 0 ? (
				<div className="no_board_list">등록된 게시글이 없습니다.</div>
			) : (
				<ul>
					{data.map((el, idx) => {
						return (
							<li key={idx}>
								<BoardItem data={el} />
							</li>
						);
					})}
				</ul>
			)}

			{data.length === 0 ? (
				""
			) : (
				<div className="pagination">
					<button className="arrow left" onClick={handleClickPrev}>
						이전
					</button>
					<ul className="page_num">
						{pagenation.map((el, idx) => {
							return (
								<li
									className={
										el === nowPage ? "page_button active" : "page_button"
									}
									key={idx}
									onClick={() => handlaClickPagination(el)}
								>
									{el}
								</li>
							);
						})}
					</ul>
					<button className="arrow right" onClick={handleClickNext}>
						다음
					</button>
				</div>
			)}
		</div>
	);
};
export default BoardListPagination;
