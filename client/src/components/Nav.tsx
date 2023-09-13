import boardData from "./../data/boardData.json";
import { ReactComponent as IconHome } from "./../assets/icon_home.svg";
import iconArrowDown from "./../assets/icon_arrow_down.svg";

const createMenuNode = (data: any[]) => {
	return data.map((el: any, idx: number) => (
		<li key={idx} className="nav_menu_item">
			{el.name ? (
				<a href="./">
					{el.name}
					<button className="toggle_updown up">
						<img src={iconArrowDown} alt="메뉴 펼치기 접기 토글 아이콘" />
					</button>
				</a>
			) : (
				<a href="./">{el.categoryName}</a>
			)}
			{el.categories && el.categories.length !== 0 ? (
				<ul className="nav_menu depth2">{createMenuNode(el.categories)}</ul>
			) : null}
		</li>
	));
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
