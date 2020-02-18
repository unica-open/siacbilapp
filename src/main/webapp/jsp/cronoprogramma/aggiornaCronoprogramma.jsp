<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
					<s:hidden name="baseUrlCronoprogramma" id="HIDDEN_baseUrl" />
					<s:form id="formAggiornaCronoprogramma" cssClass="form-horizontal" novalidate="novalidate" action="aggiornamentoCronoprogramma">

						<h3 class="h3">Progetto: <s:property value="progetto.codice" /> - <s:property value="progetto.tipoProgetto.descrizione" /> </h3>&nbsp;
						<a rel="popover" data-trigger="hover" data-original-title="Descrizione" data-content="<s:property value="progetto.descrizione" />" >
							<i class="icon-info-sign h3">&nbsp;<span class="nascosto">selezionare prima la Missione</span></i>
						</a>
						<!-- message errore -->
						<s:include value="/jsp/include/messaggi.jsp" />
						<h4>Anagrafica del Cronoprogramma</h4>
						<fieldset class="form-horizontal">
							<s:include value="/jsp/cronoprogramma/cronoprogramma_anagrafica.jsp" />
						</fieldset>
						<p>
							<a id="pulsanteApriVerificaQuadratura" class="btn btn-primary pull-right">
								Verifica quadratura&nbsp;<i class="icon-spin icon-refresh spinner"></i>
							</a>
						</p>
						<br>
						<s:include value="/jsp/cronoprogramma/cronoprogramma_dettagli.jsp" />
						<p class="margin-medium">
							<s:include value="/jsp/include/indietro.jsp" />
							<s:a action="%{baseUrlCronoprogramma + 'Clean'}" cssClass="btn btn-link">annulla</s:a>
							<s:if test="%{cronoAggiornabile}">
								<s:submit value="Salva" cssClass="btn btn-primary pull-right" />
							</s:if>
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/cronoprogramma/cronoprogramma_modali.jsp" />
	<s:if test="collegatoAProgettoDiPrevisione">
		<s:include value="/jsp/capUscitaPrevisione/selezionaCapitolo_modale.jsp">
			<s:param name="suffix">_cup</s:param>
			<s:param name="useSAC">true</s:param>
		</s:include>
		<s:include value="/jsp/capEntrataPrevisione/selezionaCapitolo_modale.jsp">
			<s:param name="suffix">_cep</s:param>
			<s:param name="useSAC">true</s:param>
		</s:include>
	</s:if><s:elseif test="collegatoAProgettoDiGestione">
		<s:include value="/jsp/capUscitaGestione/selezionaCapitolo_modale.jsp">
			<s:param name="suffix">_cug</s:param>
			<s:param name="useSAC">true</s:param>
		</s:include>
		<s:include value="/jsp/capEntrataGestione/selezionaCapitolo_modale.jsp">
			<s:param name="suffix">_ceg</s:param>
			<s:param name="useSAC">true</s:param>
		</s:include>
	</s:elseif>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}ztree/ztree.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricercaProvvedimento_collapse.js"></script>
	<script type="text/javascript" src="${jspath}capitolo/ricercaCapitoloModale.js"></script>
	<script type="text/javascript" src="${jspath}cronoprogramma/cronoprogramma.js"></script>
	<script type="text/javascript" src="${jspath}cronoprogramma/aggiorna.js"></script>
	
</body>
</html>