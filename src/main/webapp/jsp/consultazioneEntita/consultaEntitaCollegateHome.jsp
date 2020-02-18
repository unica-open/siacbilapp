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
<link rel="stylesheet" href="/siacbilapp/css/commonConsultazioneEntita.css">
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">

				<h3>Consultazione entit&agrave; collegate</h3>
				<div class="row-fluid step-content  ">
					<div id="riepilogoConsultazioneEntitaCollegate" class="span3 consultazione-entita">
						<div class="fieldset-heading">
							<h4>Riepilogo</h4>
						</div>
						<ul id ="treeConsultaEntitaCollegate" class="ztree"></ul>
					</div>

					<div id="selezioneConsultazioneEntitaCollegate"	class="span2 consultazione-entita">
						<div class="fieldset-heading">
							<h4>Selezione</h4>
						</div>
						<ul id="entitaSelezionabili" class="ztree">
							<s:iterator value="entitaConsultabili" var="ent">
								<li data-tipo-entita="<s:property value="%{#ent}"/>">
									<a>
										<span class="button ico_close"></span><span> 
											<s:property value="%{#ent.descrizione}" />
										</span>
									</a>
								</li>
							</s:iterator>
						</ul>
					</div>
					<div id="gestioneRisultatiConsultazioneEntitaCollegate"	class="span7 container-fluid consultazione-entita">

						<s:include value="/jsp/consultazioneEntita/include/selezioneEntitaDiPartenza_capitolo.jsp" />

						<s:include value="/jsp/consultazioneEntita/include/selezioneEntitaDiPartenza_provvedimento.jsp" />

						<s:include value="/jsp/consultazioneEntita/include/selezioneEntitaDiPartenza_soggetto.jsp" />
						
						<div class="hide" id="risultatiConsultazioneEntitaCollegate">
							<div  id="buttonSliding"  data-toggle="slidewidth" class=" fieldset-heading button-sliding" data-target="#selezioneConsultazioneEntitaCollegate">								
								<h4>
									<i id ="buttonSlidingIcon" data-original-title="" class="icon-double-angle-left sliding-icon icon-large tooltip-test defaultcolor"></i>&nbsp;									
									<span>Elenco <span id="tipoEntitaTrovata">risultati</span> trovati </span>
									<!-- <button id="esportaRisultaticonsultazioneEntitaCollegate" type = "submit" class="defaultcolor tooltip-test pull-right btn btn-secondary" data-target="#selezioneConsultazioneEntitaCollegate" data-original-title="DOWNLOAD RISULTATI RICERCA">
										Download risultati <i class="icon-download-alt icon-medium"></i>&nbsp;
									</button>	 -->
								</h4>
							</div>
							<div id="perInserireTabellaRisultatiConsultazioneEntitaCollegate" class=""></div>
							<div class="clearfix">
								<form id="formEsportaRisultatiConsultazioneEntita" class="form-horizontal" >
									<button id="pulsanteEsportaRisultatiConsultazioneEntita" type="submit" class="pull-left btn btn-secondary hide">
										Esporta risultati in Excel <i class="icon-download-alt icon-large"></i>&nbsp;
									</button>
									<button id="pulsanteEsportaRisultatiConsultazioneEntitaXlsx" type="submit" class="pull-left btn btn-secondary hide">
										Esporta risultati in Excel (XLSX) <i class="icon-download-alt icon-large"></i>&nbsp;
									</button>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="hide" id="iframeContainer"></div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}consultazioneEntita/gestisciGraficaConsultazioneEntita.js"></script>
	<script type="text/javascript" src="${jspath}consultazioneEntita/baseConsultazioneEntita.js"></script>
	<script type="text/javascript" src="${jspath}consultazioneEntita/ztreeSAC.js"></script>
	<script type="text/javascript" src="${jspath}consultazioneEntita/capitolo.js"></script>
	<script type="text/javascript" src="${jspath}consultazioneEntita/capitoloSpesa.js"></script>
	<script type="text/javascript" src="${jspath}consultazioneEntita/capitoloEntrata.js"></script>
	<script type="text/javascript" src="${jspath}consultazioneEntita/provvedimento.js"></script>
	<script type="text/javascript" src="${jspath}consultazioneEntita/soggetto.js"></script>
	<script type="text/javascript" src="${jspath}consultazioneEntita/alberoRiepilogoConsultazioneEntita.js"></script>
	<script type="text/javascript" src="${jspath}consultazioneEntita/consultazioneEntitaCollegate.js"></script>
</body>