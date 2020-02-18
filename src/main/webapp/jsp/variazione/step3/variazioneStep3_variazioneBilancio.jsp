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
				<s:form cssClass="form-horizontal" action="esecuzioneStep3InserimentoVariazioniImporti"
					novalidate="novalidate" id="formVariazioneStep3_VariazioneBilancio" method="post">
					<h3>Inserisci Variazione<span class="pull-right">Numero:
							<s:property value="numeroVariazione" /></span></h3>
					<div id="MyWizard" class="wizard">
						<ul class="steps">
							<li data-target="#step1" class="complete"><span
									class="badge badge-success">1</span>Scegli<span class="chevron"></span></li>
							<li data-target="#step2" class="complete"><span
									class="badge badge-success">2</span>Definisci<span class="chevron"></span></li>
							<li data-target="#step3" class="active"><span class="badge">3</span>Specifica<span
									class="chevron"></span></li>
							<li data-target="#step4"><span class="badge">4</span>Riepilogo<span class="chevron"></span>
							</li>
						</ul>
					</div>
					<div class="step-content">
						<div class="step-pane active" id="step3">
							
							<s:include value="/jsp/variazione/include/associaCapitoliAggiornamentoVariazione.jsp" />
						 <p id="bottoniInserimento" class="margin-large">
							<s:a cssClass="btn" action="returnToStep2InserimentoVariazioniImporti" id="pulsanteRedirezioneIndietro">indietro</s:a>
							<button type="button" id="aggiornaVariazioneButton" title='salva variazione' role='button' class="btn btn-primary pull-right">&nbsp;salva e prosegui&nbsp;<i class="icon-spin icon-refresh spinner" id="spinner_salva" data-spinner-async></i></button>
						 </p>
 						<s:hidden id="redirectAction" value="redirectToRiepilogoAggiornamentoVariazioneImporti"/>
 						</div>
					</div>
				</s:form>
			</div>
		</div>
	</div>





	<div id="iframeContainer"></div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}variazioni/stampaVariazioni.js"></script>
	<script type="text/javascript" src="${jspath}ztree/ztree.js"></script>
	<script type="text/javascript" src="${jspath}provvedimento/ricercaProvvedimento_collapse.js"></script>
	<script type="text/javascript" src="${jspath}variazioni/variazioni.js"></script>
	<script type="text/javascript" src="${jspath}variazioni/capitolo.variazione.js"></script>
	<script type="text/javascript" src="${jspath}variazioni/aggiorna.importi.js"></script>

</body>

</html>