import Input from "./../components/Input";
import Button from "./../components/Button";

const ValidateEmail = () => {
	return (
		<div className="input_box validate_email_box col_4">
			<h3 className="title_h3">이메일 인증</h3>
			<div className="validate_email_wrap">
				<Input
					InputLabel="이메일"
					isLabel={true}
					errorMsg="올바른 이메일을 입력하세요."
					inputAttr={{
						type: "text",
						placeholder: "회원가입시 입력한 이메일을 입력하세요.",
					}}
				/>
				<Button
					buttonType="primary"
					buttonSize="big"
					buttonLabel="인증코드 발급"
				/>
				<p className="validate_email_msg">인증코드가 전송되었습니다.</p>
				<p className="validate_email_msg error">
					인증코드 전송에 실패했습니다.
				</p>
			</div>
			<div className="content">
				<Input
					InputLabel="인증코드 확인"
					isLabel={true}
					errorMsg="입력하신 인증코드가 올바르지 않습니다."
					inputAttr={{ type: "password", placeholder: "인증코드를 입력하세요" }}
				/>
				<Button buttonType="primary" buttonSize="big" buttonLabel="입력완료" />
			</div>
		</div>
	);
};
export default ValidateEmail;
