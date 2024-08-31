import Input from "../components/Input";
import Button from "../components/Button";
import { userDataType } from "./../App";
import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { validator, ValidatorStatus } from "../assets/validater";
import iconUser from "./../assets/icon_user.svg";
import {
	getAccessToken,
	isAccessToken,
	removeAccessToken,
} from "../assets/tokenActions";

const UserEdit = ({
	userData,
	setIsLogin,
	fetchUserData,
}: {
	userData: userDataType | undefined;
	setIsLogin: React.Dispatch<React.SetStateAction<boolean>>;
	fetchUserData: () => Promise<void>;
}) => {
	const api = process.env.REACT_APP_API_URL;
	const params = useParams();
	const navigate = useNavigate();
	const [fileName, setFileName] = useState("이미지 파일을 선택하세요");
	const [file, setFile] = useState();
	const [userNickname, setUserNickname] = useState(userData?.nickname);
	const [isNameError, setIsNameError] = useState(false);

	const handleSetNickname = (value: string) => {
		setUserNickname(value);
	};
	const validatorStatusNickname: ValidatorStatus = {
		value: userNickname!,
		isRequired: true,
		valueType: "nickname",
	};

	useEffect(() => {
		let isToken = isAccessToken();
		if (isToken === false) {
			alert("로그인이 필요한 서비스입니다.");
			navigate(`/login`);
		}
	}, []);

	//닉네임 중복 검사
	const handleClickVerifyName = () => {
		const verifyNameForm = {
			nickname: userNickname,
		};
		axios
			.post(`${api}/api/v1/member/verify-nickname`, verifyNameForm)
			.then((response) => {
				alert("사용 가능한 닉네임입니다.");
			})
			.catch((error) => {
				alert("사용할 수 없는 닉네임입니다.");
			});
	};

	const handleOnChange = (event: any) => {
		setFileName(event.currentTarget.files[0].name);
		setFile(event.currentTarget.files[0]);
	};

	const handleSubmit = () => {
		const formData = new FormData();

		const data = {
			nickname: userNickname,
		};
		formData.append(
			"data",
			new Blob([JSON.stringify(data)], {
				type: "application/json",
			})
		);

		if (file) {
			formData.append("image", file);
		}
		const postAxiosConfig = {
			headers: {
				"Content-Type": "multipart/form-data", // FormData를 사용할 때 Content-Type을 변경
				Authorization: `${getAccessToken()}`,
			},
		};
		axios
			.patch(`${api}/api/v1/member`, formData, postAxiosConfig)
			.then((_) => {
				alert("회원 정보가 수정되었습니다.");
				fetchUserData();
				navigate(`/user/${userData?.id}`);
			})
			.catch((error) => {
				alert("회원 정보 수정에 실패하였습니다.");
				setUserNickname("");
				console.error(error);
			});
	};

	const handleClickMemberDelete = () => {
		const config = {
			headers: { Authorization: `${getAccessToken()}` },
		};
		//팝업창으로 대체할 것
		let isDeleteYes = window.confirm("정말로 탈퇴하시겠습니까?");
		if (isDeleteYes) {
			axios
				.delete(`${api}/api/v1/member/delete`, config)
				.then((_) => {
					alert("탈퇴되었습니다.");
					removeAccessToken();
					setIsLogin(false);
					navigate(`/`);
				})
				.catch((error) => {
					alert("회원 탈퇴에 실패하였습니다.");
				});
		}
	};

	return (
		<div className="user_edit_wrap">
			<div className="user_profile">
				<div className="profile_img_wrap">
					<img
						src={
							userData?.profileImage
								? userData?.profileImage
								: "/images/profile_defult_img.png"
						}
						alt="유저 아이콘"
						className="profile_img_default"
					/>
				</div>
				<div className="user_info_wrap">
					<p className="title_h5">{userData?.nickname}</p>
					<p className="email">{userData?.email}</p>
				</div>
			</div>
			<div className="user_input_content_wrap">
				<Input
					InputLabel="닉네임 수정"
					isLabel={true}
					errorMsg="중복된 닉네임입니다. 혹은 공백 포함 2~10자 사이의 영문, 한글, 숫자만 가능합니다."
					inputAttr={{
						type: "text",
						placeholder: "수정할 닉네임을 입력하세요",
					}}
					setInputValue={handleSetNickname}
					inputValue={userNickname}
					isError={isNameError}
				>
					<Button
						buttonType="another"
						buttonSize="big"
						buttonLabel="중복 확인"
						onClick={handleClickVerifyName}
					/>
				</Input>
				<Input
					InputLabel="프로필 이미지 수정"
					isLabel={true}
					errorMsg="이미지를 다시 업로드 해주세요."
					inputAttr={{
						type: "text",
						placeholder: "이미지를 업로드하세요",
					}}
					inputValue={fileName}
					isReadonly={true}
				>
					<button className="file_upload big">
						파일 선택
						<input
							type="file"
							onChange={(event) => handleOnChange(event)}
							id="selectImg"
							className="file_upload"
						/>
					</button>
				</Input>
				<div className="user_edit_link_wrap">
					<button className="link" onClick={() => navigate("/newPassword")}>
						비밀번호 변경하기
					</button>
					<button
						className="link member_delete"
						onClick={handleClickMemberDelete}
					>
						회원 탈퇴하기
					</button>
				</div>
			</div>
			<div className="user_edit_button_wrap">
				<Button
					buttonType="primary"
					buttonSize="big"
					buttonLabel="수정 완료"
					onClick={handleSubmit}
				/>
				<Button
					buttonType="no_em"
					buttonSize="big"
					buttonLabel="취소"
					onClick={() => navigate(`/user/${userData?.id}/edit`)}
				/>
			</div>
		</div>
	);
};
export default UserEdit;
