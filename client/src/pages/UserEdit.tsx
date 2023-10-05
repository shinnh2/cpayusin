import Input from "../components/Input";
import Button from "../components/Button";

const UserEdit = () => {
	return (
		<div className="user_edit_wrap">
			<div className="user_profile">
				<div className="profile_img_wrap"></div>
				<div className="user_info_wrap">
					<p className="title_h5">User 닉네임</p>
					<p className="email">jbaccount@gmail.com</p>
				</div>
			</div>
			<div className="user_input_content_wrap">
				<Input
					InputLabel="닉네임 수정"
					isLabel={true}
					errorMsg="중복된 닉네임입니다. 혹은 공백 포함 2~10자 사이의 영문, 한글, 숫자만 가능합니다."
					inputAttr={{
						type: "text",
						placeholder: "닉네임을 입력하세요",
					}}
				>
					<Button
						buttonType="another"
						buttonSize="big"
						buttonLabel="중복 확인"
					/>
				</Input>
				<Input
					InputLabel="프로필 이미지 수정"
					isLabel={true}
					errorMsg="중복된 닉네임입니다. 혹은 공백 포함 2~10자 사이의 영문, 한글, 숫자만 가능합니다."
					inputAttr={{
						type: "text",
						placeholder: "닉네임을 입력하세요",
					}}
				>
					<Button
						buttonType="another"
						buttonSize="big"
						buttonLabel="이미지 업로드"
					/>
				</Input>
				<a href="" className="link">
					회원 탈퇴하기
				</a>
			</div>
			<div className="user_edit_button_wrap">
				<Button buttonType="primary" buttonSize="big" buttonLabel="수정 완료" />
				<Button buttonType="no_em" buttonSize="big" buttonLabel="취소" />
			</div>
		</div>
	);
};
export default UserEdit;
