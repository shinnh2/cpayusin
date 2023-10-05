import SelectBox from "../components/SelectBox";

const Home = () => {
	const selectItem1 = ["값1", "값2", "값3"];
	return (
		<div>
			<SelectBox
				id="test1"
				selectLabel="테스트"
				isLabel={true}
				selectItem={selectItem1}
				placeHolder="선택하세요"
			/>
		</div>
	);
};
export default Home;
