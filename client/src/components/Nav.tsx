import boardData from "./../data/boardData.json";
import { ReactComponent as IconHome } from "./../assets/icon_home.svg";
import iconArrowDown from "./../assets/icon_arrow_down.svg";
import { MouseEvent, useEffect, useState, Dispatch } from "react";
import { NavLink } from "react-router-dom";
import axios from "axios";
import NavInfo from "./NavInfo";

const createMenuNode = (data: any[]) => {
	return data!.map((el: any, idx: number) => (
		<li key={idx} className="nav_menu_item">
			{el.name ? (
				<NavLink
					to={`/board/${el.id}-${el.name}`}
					onClick={clickHandler}
					className={({ isActive }) => (isActive ? "active" : "")}
				>
					{el.name}
					{el.categories && el.categories.length !== 0 ? (
						<i className="toggle_updown">
							<img src={iconArrowDown} alt="메뉴 펼치기 접기 토글 아이콘" />
						</i>
					) : null}
				</NavLink>
			) : (
				<a href="">{el.categoryName}</a>
			)}
			{el.categories && el.categories.length !== 0 ? (
				<ul className="nav_menu depth2">{createMenuNode(el.categories)}</ul>
			) : null}
		</li>
	));
};

const clickHandler = (event: React.MouseEvent<HTMLAnchorElement>): void => {
	event.currentTarget.classList.toggle("unfold");
};

const Nav = () => {
	const api = process.env.REACT_APP_API_URL;
	const [data, setData] = useState<any[]>([]);
	useEffect(() => {
		axios
			.get(`${api}/api/v1/board/menu`)
			.then((response) => {
				setData(response.data.data);
			})
			.catch((error) => {
				console.error("에러", error);
			});
	}, []);
	return (
		<nav className="nav">
			<div className="home_link">
				<a href="/">
					<IconHome />
					HOME
				</a>
			</div>
			<ul className="nav_menu depth1">{createMenuNode(data)}</ul>
			<NavInfo />
		</nav>
	);
};
export default Nav;
