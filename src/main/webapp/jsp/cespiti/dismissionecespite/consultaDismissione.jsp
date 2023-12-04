<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:form id="formInserisciTipoBene" cssClass="form-horizontal" novalidate="novalidate" action="ricercaCategoriaCespiti_effettuaRicerca">
						<h3 id="titolo">Consulta dismissione<s:property value="codice"/> </h3>
						<s:include value="/jsp/include/messaggi.jsp" />
						<s:hidden id="uidDismissioneCespite" name="dismissioneCespite.uid"/>
						<div class="step-content">
							<br/>
							<div class="step-pane active" id="step1">
								<fieldset class="form-horizontal">
									<div class="boxOrSpan2">
										<div class="boxOrInLeft">
											<ul class="htmlelt">
												<li>
													<dfn>Elenco</dfn>
													<dl><s:property value="elenco"/></dl>
												</li>
												<li>
													<dfn>Descrizione</dfn>
													<dl><s:property value="descrizione"/></dl>
												</li>
												<li>
													<dfn>Provvedimento</dfn>
													<dl><s:property value="provvedimento"/></dl>
												</li>
												<li>
													<dfn>Data cessazione</dfn>
													<dl><s:property value="dataCessazione"/></dl>
												</li>
												<li>
													<dfn>Causale dismissione</dfn>
													<dl><s:property value="causaleDismissione"/></dl>
												</li>
												<li>
													<dfn>Descrizione stato cessazione</dfn>
													<dl><s:property value="descrizioneStatoCessazione"/></dl>
												</li>
												<li>
													<dfn>Stato movimento</dfn>
													<dl><s:property value="statoMovimento"/></dl>
												</li>
											</ul>
										</div>
									</div>
								</fieldset>
								<fieldset id="fieldsetTabellaCespiti" class="form-horizontal">
									<s:include value="/jsp/cespiti/dismissionecespite/include/tabellaCespitiCollegati.jsp"/>
								</fieldset>																
							</div>
						</div>
						<p class="margin-medium">
							<s:include value="/jsp/include/indietro.jsp" />
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/dismissionecespite/tabellaCespitiCollegati.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/dismissionecespite/consultaDismissione.js"></script>
</body>
</html>