export const isAccessToken = () => {
	const token = localStorage.getItem("accessToken");
	return !!token;
};
export const getAccessToken = () => {
	if (document.cookie !== "") {
		const cookie_list = document.cookie.split(";");
		const cookieAccessToken = cookie_list[0].split("token=")[1];
		saveAccessToken(`Bearer ${cookieAccessToken}`);
	}
	return localStorage.getItem("accessToken");
};
export const saveAccessToken = (token: string) => {
	localStorage.setItem("accessToken", token);
};
export const removeAccessToken = () => {
	localStorage.removeItem("accessToken");
};
