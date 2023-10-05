import Input from "./../components/Input";
import Button from "./../components/Button";

const NewPassword = () => {
	return (
		<div className="input_box newpassword_box col_4">
			<h3 className="title_h3">새 비밀번호 입력</h3>
			<div className="content">
				<Input
					InputLabel="새 비밀번호 입력"
					isLabel={true}
					errorMsg="비밀번호는 8~20자의 영문, 숫자가 포함되어야 합니다."
					inputAttr={{ type: "password", placeholder: "새 비밀번호를 입력하세요" }}
				/>
				<Input
					InputLabel="새 비밀번호 확인"
					isLabel={true}
					errorMsg="비밀번호가 일치하지 않습니다."
					inputAttr={{ type: "password", placeholder: "새 비밀번호를 한 번 더 입력하세요" }}
				/>
				<Button buttonType="primary" buttonSize="big" buttonLabel="입력 완료" />
			</div>
		</div>
	);
};
export default NewPassword;
