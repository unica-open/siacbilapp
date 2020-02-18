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
			<div class="span12 contentPage">
				<s:include value="/jsp/include/messaggi.jsp" />
				<s:form cssClass="form-horizontal" data-richiedi-conferma="richiedi" action="concludiAggiornamentoVariazioneImporti" novalidate="novalidate" id="aggiornaVariazioneImporti" method="post" >
					<h3>Aggiorna Variazione</h3>
					<div class="accordion-group">
						<div class="accordion-heading">
							<a class="accordion-toggle" href="#collapseVariazioni" data-parent="#accordion2" data-toggle="collapse">
								Variazione<span class="icon"></span>
							</a>
						</div>
						<div id="collapseVariazioni" class="accordion-body in collapse" style="height: auto;">
							<div class="accordion-inner">
								<!-- SIAC 6884 -->
								<s:include value="/jsp/variazione/include/associaCapitoliAggiornamentoVariazione.jsp" />
								<br/>
							</div>
						</div> <!-- CHIUSURA COLLAPSE -->
					</div><!-- CHIUSURA ACCORDION-GROUP -->
					
					
					<p id="bottoni" class="margin-large">
						<s:include value="/jsp/include/indietro.jsp" />
						&nbsp;<span class="nascosto"> | </span>
						<!-- SIAC 6884 -->
						<s:reset cssClass="btn btn-link" value="annulla" />
						&nbsp;<span class="nascosto"> | </span>
						<button type="button" id="aggiornaVariazioneButton" title='salva variazione' role='button' class="btn">&nbsp;salva&nbsp;<i class="icon-spin icon-refresh spinner" id="spinner_salva" data-spinner-async></i></button>
						&nbsp;<span class="nascosto"> | </span>
						<button type = "button" id = "annullaVariazioneButton" type="button" title='annulla variazione' role='button' class="btn">annulla variazione</button>
						&nbsp;<span class="nascosto"> | </span>
						
						<s:if test="idAzioneReportVariazioni != null">
							<button data-parametri-stampa="Numero Variazione:<s:property value="numeroVariazione" />;Anno Variazione:<s:property value="annoCompetenza" />;Anno Competenza:<s:property value="annoCompetenza" />" type="button" id="stampaVariazioneButton" type="button" title='stampa variazione' role='button' class="btn">stampa</button>
							&nbsp;<span class="nascosto"> | </span>
						</s:if>
						
						
						<s:if test="variazioneDecentrataAperta">
							<button type = "button" id="chiudiPropostaButton" type="button" title='chiudi proposta' role='button' class="btn btn-primary pull-right">&nbsp;chiudi proposta<i class="icon-spin icon-refresh spinner" id="spinner_concludi" data-spinner-async></i></button>
						</s:if><s:else>
							<button type = "button" id="concludiVariazioneButton" type="button" title='concludi attività variazione' role='button' class="btn btn-primary pull-right">&nbsp;concludi attività&nbsp;<i class="icon-spin icon-refresh spinner" id="spinner_concludi" data-spinner-async></i></button>
						</s:else>
					
					</p>
					<s:hidden id="redirectAction" value="redirectToDisabledAggiornamentoVariazioneImporti"/>
					
				</s:form>
			</div>
		</div>
	</div>

	
	

	
	<%-- Modale annulla variazione --%>
	
	<%-- /Modale annulla --%>
	<%-- <div id="iframeContainer"></div>
	
	<s:include value="/jsp/include/modaleStampaVariazioni.jsp" /> --%>

	<s:include value="/jsp/include/modaleConfermaProsecuzioneSuAzione.jsp" />
	<s:include value="/jsp/include/footer.jsp"/>
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}variazioni/stampaVariazioni.js"></script>
	<script type="text/javascript" src="${jspath}ztree/ztree.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricercaProvvedimento_collapse.js"></script>
	<script type="text/javascript" src="${jspath}variazioni/variazioni.js"></script>
	<script type="text/javascript" src="${jspath}variazioni/capitolo.variazione.js"></script>
	<script type="text/javascript" src="${jspath}variazioni/aggiorna.importi.js"></script>

</body>
</html>