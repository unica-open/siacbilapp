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
			<div class="span12 contentPage">
				<s:hidden name="baseUrl" id="HIDDEN_baseUrl" />
				
				<s:form cssClass="form-horizontal" action="#" novalidate="novalidate" id="formCausaleEP">
					<s:include value="/jsp/include/messaggi.jsp" />
					<s:hidden name="causaleEP.uid" id="uidCausaleEP" />
					<s:hidden name="richiestaEconomale.statoOperativoCausaleEP" id="statoOperativoCausaleEPCausaleEP" />
					<h3><s:property value="denominazioneWizard"/></h3>
					<s:if test="isAggiornamento">
						<h3><s:property value="stringaRiepilogoRichiestaEconomale" /></h3>
					</s:if>
					<div class="wizard" id="MyWizard">
						<ul class="steps">
							<li class="complete" data-target="#step1"><span class="badge badge-success">1</span>Dati principali<span class="chevron"></span></li>
							<li class="complete" data-target="#step2"><span class="badge badge-success">2</span>Dettaglio conti associati<span class="chevron"></span></li>
							<li class="active" data-target="#step3"><span class="badge">3</span>Riepilogo<span class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div id="step3" class="step-pane active">
							<s:include value="/jsp/contabilitaGenerale/causali/include/fieldsetConsultazioneCausale.jsp" />
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/causali/inserisci.aggiorna.step3.js"></script>
	
</body>
</html>