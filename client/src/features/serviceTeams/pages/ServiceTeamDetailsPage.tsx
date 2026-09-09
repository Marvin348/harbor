import { useParams } from "react-router-dom";

export const ServiceTeamDetailsPage = () => {
  const { id } = useParams();

  if (!id) return null;

  return <div>ServiceTeamDetailsPage</div>;
};
