import SelectBox from "../components/SelectBox";
import Admin from "./Admin";

const Home = () => {
	return (
		<div>
			<img
				className="home_main"
				src={`${process.env.PUBLIC_URL}/images/home_banner.png`}
			/>
			<img
				className="home_main"
				src={`${process.env.PUBLIC_URL}/images/home_main_1.jpg`}
			/>
			<img
				className="home_main"
				src={`${process.env.PUBLIC_URL}/images/home_main_2.jpg`}
			/>
		</div>
	);
};
export default Home;
