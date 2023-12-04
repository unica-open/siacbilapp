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
				<s:form cssClass="form-horizontal" method="post" action="cassaEconomaleStampe_enterStep3" novalidate="novalidate" id="formStampaRendicontoCassa">
					<s:hidden name="HIDDEN_stampaDefinitiva" name="stampaDefinitiva" />
					<s:hidden name="HIDDEN_startPage" name="startPage" />
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Rendiconto Periodico, da: <s:property value="periodoDaRendicontareDataInizio" /> &ndash; a: <s:property value="periodoDaRendicontareDataFine"/></h3>
					
					<h4> Numero operazione di cassa in stampa: <s:property value="%{numeroOperazioniDiCassa}" /></h4>
					<!-- 
					<s:if test='%{numeroOperazioniDiCassa == 0}'>
						<H4> Numero operazione di cassa in stampa <s:property value="%{numeroOperazioniDiCassa}" /></H4>
					</s:if>
					-->
					
					<fieldset class="form-horizontal">
						<br />
						<div class="step-content">
							<div class="step-pane active" id="step1">
							
								<table class="table table-striped table-bordered table-hover dataTable" id="risultatiRicerca" summary="...." >
									<thead>
										<tr>
											<th scope="col">Selez.</th>
											<th scope="col">Capitolo</th>
											<th scope="col">Impegno</th>
											<th scope="col"><abbr title="Numero">N.</abbr> Movimento</th>
											<th scope="col">Tipo</th>
											<th scope="col">Data Registrazione</th>
											<th scope="col">Beneficiario</th>
											<th scope="col">Oggetto</th>
											<th scope="col">Importo</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									<tfoot>
										<tr>
											<th scope="col">TOTALE</th>
											<th scope="col"></th>
											<th scope="col"></th>
											<th scope="col" class="text-right"><s:property value="numeroTotaleMovimenti"/></th>
											<th scope="col"></th>
											<th scope="col"></th>
											<th scope="col"></th>
											<th scope="col"></th>
											<th scope="col" class="text-right"><s:property value="descrizioneImportoTotale"/></th>
										</tr>
									</tfoot>
								</table>
							</div>
						</div>

					</fieldset>
					<p class="margin-large">
						<s:a cssClass="btn" action="cassaEconomaleStampe_backToStep1" id="pulsanteRedirezioneIndietro">indietro</s:a>
						<s:if test="stampaDefinitiva">
							<button type="submit" class="btn btn-primary pull-right">prosegui</button>
						</s:if><s:else>
							<button type="button" class="btn btn-primary pull-right" id="pulsanteStampa">stampa</button>
						</s:else>
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	
									

	
	<s:if test="!stampaDefinitiva">
		<s:include value="/jsp/cassaEconomale/stampe/rendiconto/modaleConfermaStampa.jsp" />
	</s:if>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/cassaEconomale/stampe/stampaCECRendiconto.step2.js"></script>

</body>
</html>