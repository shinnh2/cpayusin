import Input from "./../components/Input";
import Button from "./../components/Button";

const Login = () => {
	return (
		<div className="login_box col_4">
			<h3 className="title_h3">로그인</h3>
			<div className="content">
				<Input
					InputLabel="이메일"
					isLabel={true}
					errorMsg="올바른 이메일을 입력해 주세요."
					inputAttr={{ type: "text", placeholder: "이메일을 입력하세요" }}
				/>
				<Input
					InputLabel="비밀번호"
					isLabel={true}
					errorMsg="올바른 비밀번호를 입력해 주세요."
					inputAttr={{ type: "password", placeholder: "비밀번호를 입력하세요" }}
				/>
				<Button buttonType="primary" buttonSize="big" buttonLabel="로그인" />
			</div>
		</div>
	);
};
export default Login;
