import { useParams } from "@tanstack/react-router";

export const ServiceTeamDetailsPage = () => {
  const { id } = useParams({ strict: false });

  if (!id) return null;

  return <div>ServiceTeamDetailsPage</div>;
};
