import { Outlet } from "react-router-dom";

export const AppLayout = () => {

  return (
    <section>
      <h1>Hallo von Harbor</h1>

      <div>
        <Outlet />
      </div>
    </section>
  );
};
