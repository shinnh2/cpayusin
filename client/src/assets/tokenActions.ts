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
	if (document.cookie !== "") {
		document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/";
	}
	localStorage.removeItem("accessToken");
};
