import boardData from "./../data/boardData.json";
import { ReactComponent as IconHome } from "./../assets/icon_home.svg";
import iconArrowDown from "./../assets/icon_arrow_down.svg";
import { MouseEvent } from "react";

const createMenuNode = (data: any[]) => {
	return data.map((el: any, idx: number) => (
		<li key={idx} className="nav_menu_item">
			{el.name ? (
				<a href="" onClick={clickHandler}>
					{el.name}
					{el.categories && el.categories.length !== 0 ? (
						<i className="toggle_updown">
							<img src={iconArrowDown} alt="메뉴 펼치기 접기 토글 아이콘" />
						</i>
					) : null}
				</a>
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
	event.preventDefault();
	console.log(event.currentTarget.classList);
	event.currentTarget.classList.toggle("unfold");
};

const Nav = () => {
	return (
		<nav className="nav">
			<div className="home_link">
				<a href="./">
					<IconHome />
					HOME
				</a>
			</div>
			<ul className="nav_menu depth1">{createMenuNode(boardData)}</ul>
		</nav>
	);
};
export default Nav;
