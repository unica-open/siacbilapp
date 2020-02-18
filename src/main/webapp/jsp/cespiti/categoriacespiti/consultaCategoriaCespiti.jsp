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
						<h3 id="titolo">Consulta categoria cespite </h3>
						<s:include value="/jsp/include/messaggi.jsp" />
						<div class="step-content">
							<br/>
							<div class="step-pane active" id="step1">
								<fieldset class="form-horizontal">
									<ul class="htmlelt">
									<li>
										<dfn>Codice</dfn>
										<dl><s:property value="categoriaCespiti.codice"/></dl>
									</li>
									<li>
										<dfn>Descrizione</dfn>
										<dl><s:property value="categoriaCespiti.descrizione"/></dl>
									</li>
									<li>
										<dfn>Aliquota annua &#037;</dfn>
										<dl><s:property value="categoriaCespiti.aliquotaAnnua"/></dl>
									</li>
									<li>
										<dfn>Calcolo primo anno</dfn>
										<dl><s:if test="%{categoriaCespiti.categoriaCalcoloTipoCespite != null}">
												<s:property value="categoriaCespiti.categoriaCalcoloTipoCespite.descrizione"/>
											</s:if>
										</dl>
									</li>
								</ul>
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
	<script type="text/javascript" src="${jspath}cespiti/categoriacespiti/consultaCategoriaCespiti.js"></script>
</body>
</html>