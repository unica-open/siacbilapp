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
	<div id="contentPanel">
		<div id="centerWrapper">
			<div id="centerPanel">
				<div>
					<div>
						<div class="alert alert-${cssClassName}">
							<button type="button" class="close" data-hide="alert">&times;</button>
							<strong>Attenzione!!</strong><br>
							<ul>
								<li><s:property value="message"/></li>
							</ul>
						</div>
					</div>
					<div id="applicationError_links">
						<s:include value="/jsp/include/indietro.jsp" />
					</div>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
</body>
</html>