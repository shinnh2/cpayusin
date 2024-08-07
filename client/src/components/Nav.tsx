import boardData from "./../data/boardData.json";
import { ReactComponent as IconHome } from "./../assets/icon_home.svg";
import iconArrowDown from "./../assets/icon_arrow_down.svg";
import { MouseEvent, useEffect, useState, Dispatch } from "react";
import { NavLink } from "react-router-dom";
import NavInfo from "./NavInfo";

const Nav = ({
	menuData,
	setIsNavDrawerOn,
}: {
	menuData: any[];
	setIsNavDrawerOn: React.Dispatch<React.SetStateAction<boolean>> | undefined;
}) => {
	const api = process.env.REACT_APP_API_URL;
	const [unfold, setUnfold] = useState(false);
	const createMenuNode = (data: any[]) => {
		return data!.map((el: any, idx: number) => (
			<li key={idx} className="nav_menu_item">
				{el.name ? (
					<NavLink
						to={`/board/${el.name}`}
						onClick={clickMenuHandler}
						className={({ isActive }) => (isActive ? "active" : "")}
					>
						{el.name}
						{el.category && el.category.length !== 0 ? (
							<button className="toggle_updown" onClick={clickToggleHandler}>
								<img src={iconArrowDown} alt="메뉴 펼치기 접기 토글 아이콘" />
							</button>
						) : null}
					</NavLink>
				) : (
					<a href="">{el.categoryName}</a>
				)}
				{el.category && el.category.length !== 0 ? (
					<ul className={unfold ? "nav_menu depth2 unfold" : "nav_menu depth2"}>
						{createMenuNode(el.category)}
					</ul>
				) : null}
			</li>
		));
	};

	const clickMenuHandler = (
		event: React.MouseEvent<HTMLAnchorElement>
	): void => {
		if (setIsNavDrawerOn !== undefined) setIsNavDrawerOn(false);
	};
	const clickToggleHandler = (
		event: React.MouseEvent<HTMLButtonElement>
	): void => {
		event.currentTarget.classList.toggle("unfold");
		event.preventDefault();
		event.stopPropagation();
		setUnfold((prev) => !prev);
	};

	return (
		<nav className="nav">
			<div className="home_link">
				<a href="/">
					<IconHome />
					HOME
				</a>
			</div>
			<ul className="nav_menu depth1">{createMenuNode(menuData)}</ul>
			<NavInfo />
		</nav>
	);
};
export default Nav;
