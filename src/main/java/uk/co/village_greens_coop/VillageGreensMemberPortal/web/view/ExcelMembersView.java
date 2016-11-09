package uk.co.village_greens_coop.VillageGreensMemberPortal.web.view;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;
import org.springframework.web.servlet.view.document.AbstractXlsxView;
import uk.co.village_greens_coop.VillageGreensMemberPortal.model.api.MemberRow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExcelMembersView extends AbstractXlsxStreamingView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
									  Workbook workbook, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {

		@SuppressWarnings("unchecked")
		List<MemberRow> members = (List<MemberRow>) model.get("members");

		String todaysDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"Village Greens Members - %s.xlsx\"", todaysDate));

		//create a wordsheet
		Sheet sheet = workbook.createSheet("All Members");
 
		Row header = sheet.createRow(0);

		int col = 0;
		header.createCell(col++).setCellValue("Member No");
		header.createCell(col++).setCellValue("Status");
		header.createCell(col++).setCellValue("Title");
		header.createCell(col++).setCellValue("FirstName");
		header.createCell(col++).setCellValue("Surname");
		header.createCell(col++).setCellValue("Organisation");
		header.createCell(col++).setCellValue("Email");
		header.createCell(col++).setCellValue("AddressLine1");
		header.createCell(col++).setCellValue("AddressLine2");
		header.createCell(col++).setCellValue("AddressLine3");
		header.createCell(col++).setCellValue("AddressLine4");
		header.createCell(col++).setCellValue("Postcode");
		header.createCell(col++).setCellValue("DOB");
		header.createCell(col++).setCellValue("Investment");
		header.createCell(col++).setCellValue("AmountPaid");
		header.createCell(col++).setCellValue("AmountOverdue");
		header.createCell(col++).setCellValue("RollCall");
		header.createCell(col++).setCellValue("SEIS");
		header.createCell(col++).setCellValue("Committee");
		header.createCell(col++).setCellValue("CertificateGenerated");
		header.createCell(col++).setCellValue("CertificateSent");

		int rowNum = 1;
		try {
			for (MemberRow member : members) {
				//create the row data
				col = 0;
				Row row = sheet.createRow(rowNum++);
				row.createCell(col++).setCellValue(nullOrBlank(member.getMemberNo()));
				row.createCell(col++).setCellValue(nullOrBlank(member.getMemberStatus()));
				row.createCell(col++).setCellValue(nullOrBlank(member.getTitle()));
				row.createCell(col++).setCellValue(nullOrBlank(member.getFirstName()));
				row.createCell(col++).setCellValue(nullOrBlank(member.getSurname()));
				row.createCell(col++).setCellValue(nullOrBlank(member.getOrganisation()));
				row.createCell(col++).setCellValue(nullOrBlank(member.getEmail()));
				row.createCell(col++).setCellValue(nullOrBlank(member.getAddressLine1()));
				row.createCell(col++).setCellValue(nullOrBlank(member.getAddressLine2()));
				row.createCell(col++).setCellValue(nullOrBlank(member.getAddressLine3()));
				row.createCell(col++).setCellValue(nullOrBlank(member.getAddressLine4()));
				row.createCell(col++).setCellValue(nullOrBlank(member.getPostcode()));
				row.createCell(col++).setCellValue(nullOrBlank(member.getDob()));
				row.createCell(col++).setCellValue("£" + new DecimalFormat("###,###").format(member.getTotalInvestment()));
				row.createCell(col++).setCellValue("£" + new DecimalFormat("###,###").format(member.getAmountPaid()));
				row.createCell(col++).setCellValue("£" + new DecimalFormat("###,###").format(member.getAmountOverdue()));
				row.createCell(col++).setCellValue(nullOrBlank(member.getRollCall()));
				row.createCell(col++).setCellValue(nullOrBlank(member.getSeis()));
				row.createCell(col++).setCellValue(nullOrBlank(member.getCommittee()));
				row.createCell(col++).setCellValue(nullOrBlank(member.getCertificateGenerated()));
				row.createCell(col++).setCellValue(nullOrBlank(member.getCertificateSent()));
			}
		}catch (Throwable t) {
			System.out.println(t.getMessage());
		}
	}

	private String nullOrBlank(String str) {
		return (str == null) ? "" : str;
	}

	private String nullOrBlank(Long l) {
		return (l == null) ? "" : Long.toString(l);
	}

	private String nullOrBlank(Boolean b) {
		return (b == null) ? "" : Boolean.toString(b);
	}
}
