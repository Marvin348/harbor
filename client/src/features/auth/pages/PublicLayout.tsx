import { Outlet } from "@tanstack/react-router";

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
