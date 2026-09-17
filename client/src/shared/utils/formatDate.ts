export const formatDate = (dateString: string): string => {
  if (!dateString) return "";

  const date = new Date(dateString);

  return date.toLocaleDateString("de-DE", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  });
};
