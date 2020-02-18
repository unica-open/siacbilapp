<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>	
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<fmt:setBundle basename="globalMessages"/>
	<s:include value="/jsp/include/header.jsp" />
	<s:form id="utenteNonConfigurato" name="utenteNonConfigurato" action="utenteNonConfigurato" method="post">
		<div id="contentPanel">
			<div id="centerWrapper">
				<div id="centerPanel">
					<div>
						<h3><s:text name="utenteNonConfigurato.error.label" /></h3>
						<div id="applicationError">
							<fmt:message key="error.utenteNonConfigurato.message" />
						</div>
						<div id="applicationError_links">
							<div>
								<s:a action="redirectToCruscotto" cssClass="buttonWidget"><fmt:message key="error.fatalerror.btnmsg" /></s:a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>		
	</s:form>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
</body>
</html>