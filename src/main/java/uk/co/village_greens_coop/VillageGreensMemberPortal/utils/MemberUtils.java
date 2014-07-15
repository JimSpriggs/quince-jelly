package uk.co.village_greens_coop.VillageGreensMemberPortal.utils;

public class MemberUtils {
	public static String getDisplayName(String organisation, String firstName, String surname) {
		String displayName = "";
		if (organisation != null && !organisation.equals("")) {
			displayName = organisation;
		} else {
			if (surname != null && !surname.equals("")) {
				displayName = surname;
			}
			
			if (firstName != null && !firstName.equals("")) {
				if (!displayName.equals("")) {
					displayName += ", " + firstName;
				} else {
					displayName = firstName;
				}
			}
		}
		
		return displayName;
	}

	public static String getFullDisplayName(String organisation, String title, String firstName, String surname) {
		String displayName = "";
		if (organisation != null && !organisation.equals("")) {
			displayName = organisation;
		}
		
		String fullName = "";
		if (title != null && !title.equals("")) {
			fullName = title;
		}

		if (firstName != null && !firstName.equals("")) {
			if (!fullName.equals("")) {
				fullName += " " + firstName;
			} else {
				fullName = firstName;
			}
		}
		
		if (surname != null && !surname.equals("")) {
			if (!fullName.equals("")) {
				fullName += " " + surname;
			} else {
				fullName = surname;
			}
		}
		
		if (fullName != "") {
			if (displayName != "") {
				// we have an organisation name and a person's name
				displayName += " (" + fullName + ")";
			} else {
				displayName = fullName;
			}
		}
		
		return displayName;
	}
}
