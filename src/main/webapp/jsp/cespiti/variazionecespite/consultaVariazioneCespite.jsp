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
			<div class="span12">
				<div class="contentPage">
					<s:form cssClass="form-horizontal" novalidate="novalidate">
						<h3 id="titolo"><s:property value="formTitle"/></h3>
						<s:include value="/jsp/include/messaggi.jsp" />
						<div class="step-content">
							<div class="step-pane active" id="step1">
								
								<div class="boxOrInLeft">
									<p><s:property value="boxTitle" /></p>
									<ul class="htmlelt">
										<li>
											<dfn>Anno</dfn>
											<dl><s:property value="variazioneCespite.annoVariazione" />&nbsp;</dl>
										</li>
										<li>
											<dfn>Data inserimento</dfn>
											<dl><s:property value="variazioneCespite.dataVariazione" />&nbsp;</dl>
										</li>
										<li>
											<dfn>Tipo variazione</dfn>
											<dl><s:property value="tipoVariazione" />&nbsp;</dl>
										</li>
										<li>
											<dfn>Importo variazione</dfn>
											<dl><s:property value="variazioneCespite.importo" />&nbsp;</dl>
										</li>
										<li>
											<dfn>Descrizione</dfn>
											<dl><s:property value="variazioneCespite.descrizione" />&nbsp;</dl>
										</li>
										<li>
											<dfn>Stato</dfn>
											<dl><s:property value="statoVariazioneCespite" />&nbsp;</dl>
										</li>
									</ul>
								</div>
								<div class="boxOrInRight">
									<p>Dati cespite</p>
									<ul class="htmlelt">
										<li>
											<dfn>Codice</dfn>
											<dl><s:property value="variazioneCespite.cespite.codice" />&nbsp;</dl>
										</li>
										<li>
											<dfn>Descrizione</dfn>
											<dl><s:property value="variazioneCespite.cespite.descrizione" />&nbsp;</dl>
										</li>
										<li>
											<dfn>Tipo bene</dfn>
											<dl><s:property value="tipoBeneCespite" />&nbsp;</dl>
										</li>
										<li>
											<dfn>Classificazione giudiziaria</dfn>
											<dl><s:property value="classificazioneGiudiziariaCespite" />&nbsp;</dl>
										</li>
										<li>
											<dfn>Stato</dfn>
											<dl><s:property value="statoCespite" />&nbsp;</dl>
										</li>
										<li>
											<dfn>Valore iniziale</dfn>
											<dl><s:property value="variazioneCespite.cespite.valoreIniziale" />&nbsp;</dl>
										</li>
										<li>
											<dfn>Valore attuale</dfn>
											<dl><s:property value="variazioneCespite.cespite.valoreAttuale" />&nbsp;</dl>
										</li>
									</ul>
								</div>
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
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/variazionecespite/consulta.js"></script>
</body>
</html>