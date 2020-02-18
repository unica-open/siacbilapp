<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />

</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="span12 contentPage">
					<s:form action="" cssClass="form-horizontal" novalidate="novalidate">
						<s:include value="/jsp/include/messaggi.jsp" />
						<h3><s:property value="intestazione"/></h3>
						<h4><s:property value="stato"/></h4>
						<div class="step-pane active" id="step1">
							<fieldset class="form-horizontal">
								<s:include value="/jsp/contabilitaGenerale/registrazione/consultazione/consultaDati%{consultazioneSubpath}.jsp" />
							</fieldset>
						</div>

						<p class="margin-large">
							<s:include value="/jsp/include/indietro.jsp" />
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}contabilitaGenerale/registrazione/consultaRegistrazioneMovFin${consultazioneSubpath}.js"></script>

</body>
</html>