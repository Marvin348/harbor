import {
  Headphones,
  type LucideIcon,
  MapPin,
  ShieldCheck,
  Users,
} from "lucide-react";

type ServiceTeamTemplate = {
  name: string;
  description: string;
  icon: LucideIcon;
};

export const SERVICE_TEAM_TEMPLATES: ServiceTeamTemplate[] = [
  {
    name: "IT Support",
    description:
      "Hardware, Software, Zugänge und technische Störungen zentral bearbeiten.",
    icon: Headphones,
  },
  {
    name: "People Operations",
    description:
      "Onboarding, Bescheinigungen und interne HR-Anfragen strukturiert bündeln.",
    icon: Users,
  },
  {
    name: "Facility Management",
    description:
      "Räume, Standorte, Zutritt und Arbeitsplatz-Ausstattung koordinieren.",
    icon: MapPin,
  },
  {
    name: "Information Security",
    description:
      "Freigaben, Risiko-Meldungen und sicherheitsrelevante Anfragen prüfen.",
    icon: ShieldCheck,
  },
];
