import { Navigate } from "react-router-dom";
import { ICheckRoutesProps } from "./ICheckRoutesProps";
import { useAuthContext } from "../hooks/useAuthContext";

export const CheckPrivateRoutes: React.FC<ICheckRoutesProps> = ({
	children,
}) => {
	const { authState } = useAuthContext();
	const { logged } = authState;
	return logged ? children : <Navigate to="/login" />;
};
