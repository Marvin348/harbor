import { Outlet } from "react-router-dom";

export const PublicLayout = () => {
  return (
    <div>
      <h2>PublicLayout</h2>
      <div>
        <Outlet />
      </div>
    </div>
  );
};
