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
						<h3 id="titolo">Consulta tipo bene </h3>
						<s:include value="/jsp/include/messaggi.jsp" />
						<div class="step-content">
							<br/>
							<div class="step-pane active" id="step1">
								<fieldset class="form-horizontal">
									<div class="boxOrSpan2">
										<div class="boxOrInLeft">
											<ul class="htmlelt">
												<li>
													<dfn>Codice</dfn>
													<dl><s:property value="tipoBeneCespite.codice"/></dl>
												</li>
												<li>
													<dfn>Descrizione</dfn>
													<dl><s:property value="tipoBeneCespite.descrizione"/></dl>
												</li>
												<li>
													<dfn>Categoria cespite</dfn>
													<dl><s:property value="categoriaCespite"/></dl>
												</li>
												<li>
													<dfn>Conto patrimoniale</dfn>
													<dl><s:property value="contoPatrimonialeTipoBene"/></dl>
												</li>
												<li>
													<dfn>Conto di ammortamento</dfn>
													<dl><s:property value="contoAmmortamentoTipoBene"/></dl>
												</li>
												<li>
													<dfn>Evento di ammortamento</dfn>
													<dl><s:property value="eventoAmmortamentoTipoBene"/></dl>
												</li>
												<li>
													<dfn>Causale di ammortamento</dfn>
													<dl><s:property value="causaleAmmortamentoTipoBene"/></dl>
												</li>
												<li>
													<dfn>Testo scrittura ammortamento</dfn>
													<dl><s:property value="testoScritturaAmmortamento"/></dl>
												</li>
												<li>
													<dfn>Conto del fondo di ammortamento</dfn>
													<dl><s:property value="contoFondoAmmortamentoTipoBene"/></dl>
												</li>
											</ul>								
										</div>
										<div class="boxOrInRight">
											<ul class="htmlelt">
												
												<li>
													<dfn>Conto plusvalenza da alienazione</dfn>
													<dl><s:property value="contoPlusvalenzaTipoBene"/></dl>
												</li>
												<li>
													<dfn>Conto di minusvalenza da alienazione</dfn>
													<dl><s:property value="contoMinusvalenzaTipoBene"/></dl>
												</li>
												<li>
													<dfn>Conto di incremento valore</dfn>
													<dl><s:property value="contoIncrementoTipoBene"/></dl>
												</li>
												<li>
													<dfn>Evento di incremento valore</dfn>
													<dl><s:property value="eventoIncrementoTipoBene"/></dl>
												</li>
												<li>
													<dfn>Causale di incremento valore</dfn>
													<dl><s:property value="causaleIncrementoTipoBene"/></dl>
												</li>
												<li>
													<dfn>Conto di decremento valore</dfn>
													<dl><s:property value="contoDecrementoTipoBene"/></dl>
												</li>
												<li>
													<dfn>Evento di decremento valore</dfn>
													<dl><s:property value="eventoDecrementoTipoBene"/></dl>
												</li>
												<li>
													<dfn>Causale di decremento valore</dfn>
													<dl><s:property value="causaleDecrementoTipoBene "/></dl>
												</li>
												<li>
													<dfn>Conto credito di alienazione</dfn>
													<dl><s:property value="contoAlienazioneTipoBene"/></dl>
												</li>
												<li>
													<dfn>Conto donazione / rinvenimento</dfn>
													<dl><s:property value="contoDonazioneTipoBene"/></dl>
												</li>
											</ul>
										</div>
									</div>
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
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/tipobenecespite/consultaTipoBene.js"></script>
</body>
</html>