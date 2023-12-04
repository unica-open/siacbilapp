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

	<hr />
	<div class="container-fluid-banner">
		<a name="A-contenuti" title="A-contenuti"></a>
	</div>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
			<s:include value="/jsp/include/messaggi.jsp" />

					<ul class="nav nav-tabs">
						<li class="active">
							<a href="#intestazione" data-toggle="tab" id="linkTabIntestazione">Consulta PagoPA</a>
						</li>
					</ul>

					<div class="tab-content">

						<!-- SIAC-8046 CM  12/04/2021 Task 2.2 Inizio -->
						<div class="alert alert-success hide " id="INFORMAZIONI">
							<button type="button" class="close" data-hide="alert">&times;</button>
							<strong>Informazioni</strong><br>
							<ul></ul>
						</div> 
						<!-- SIAC-8046 CM  12/04/2021 Task 2.2 Fine -->
						<div class="tab-pane active" id="intestazione">
							<div class="boxOrSpan2">
								<div class="">
									<h4>Intestazione</h4>
									<ul class="htmlelt">
										<li>
											<dfn>Numero Provvisorio</dfn>
											<dl><s:property value="%{numeroProvvisorio}" /></dl>
										</li>
										
										<li>
											<dfn>Data Emissione Provvisorio</dfn>
											<dl><s:property value="%{dataEmissioneProvvisorio}" /></dl>
										</li>
										<li>
											<dfn>Importo Provvisorio</dfn>
											<dl><s:property value="%{importoProvvisorio}" /></dl>
										</li>
										<li>
											<dfn>Data Elaborazione</dfn>
											<dl><s:property value="%{dataElaborazione}" /></dl>
										</li>
										
										<li>
											<dfn>Stato Elaborazione</dfn>
											<dl><s:property value="%{statoElaborazione}" /></dl>
										</li>
									</ul>
								</div>
								
								<div class="">
									<!-- TABELLA RICONCILIAZIONE -->
										<h4><span id="id_num_result" class="num_result"></span></h4>

							<table class="table table-hover tab_left dataTable" id="risultatiRicercaRiconciliazioni" summary="....">
							<thead>
								<tr>
									<th scope="col" style="text-align: center;">Codice Voce</th>
									<th scope="col" style="text-align: center;">Descrizione Voce</th>
									<th scope="col" style="text-align: center;">Codice Sottovoce</th>
									<th scope="col" style="text-align: center;">Anno e Numero Accertamento</th>
									<!-- SIAC-8046 CM 16/03/2021 Task 2.0 Inizio -->
									<th scope="col" style="text-align: center;">Nuovo Anno e Numero Accertamento</th>
									<!-- SIAC-8046 CM 16/03/2021 Task 2.0 Fine -->
									<th scope="col" style="text-align: center;">Importo Riga Riconciliazione</th>
									<th scope="col" style="text-align: center; width: 300px;">Esito</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
							<tfoot>
							</tfoot>
						</table>

						<s:hidden id="HIDDEN_startPosition" name="startPosition" value="%{savedDisplayStart}" />
								
								</div>
							</div>
						</div>
						</div>


					</div>
						<p>						  
							<s:include value="/jsp/include/indietro.jsp" />
						</p>
						
						
	<!-- MODAL FATTURE-->
	<div id="erroriRiconciliazioniFattureModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="dettagliErroriFatture" aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3 id="dettagliErroriFatture">Dettagli Errori</h3>
		</div>
		<div class="modal-body">
			<fieldset class="form-horizontal" id="yyyyyy">
				<div class="control-group">
					<table class="table table-hover tab_left dataTable" id="risultatiRiconciliazioneFattureErrori" summary="....">
							<thead>
								<tr>
									<th scope="col" style="text-align: center;">Descrizione</th>
									<th scope="col" style="text-align: center;">Tipo Operazione Documento</th>
									<th scope="col" style="text-align: center;" class="tipoFatture">Codice Fiscale</th>
									<th scope="col" style="text-align: center;" class="tipoFatture">Ragione Sociale</th>
									<th scope="col" style="text-align: center;" class="tipoFatture">Importo</th>
									<th scope="col" style="text-align: center;" class="tipoFatture">IUV</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
							<tfoot>
							</tfoot>
						</table>
						<s:hidden id="HIDDEN_startPosition_errori_fatture" name="startPosition" value="%{savedDisplayStart}" />
				</div>
			</fieldset>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-primary pull-right" data-dismiss="modal">
				Chiudi
			</button>
		</div>
	</div>



<!-- MODAL SINTETICA-->
	<div id="erroriRiconciliazioniModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="dettagliErrori" aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3 id="dettagliErrori">Dettagli Errori</h3>
		</div>
		<div class="modal-body">
			<fieldset class="form-horizontal" id="yyyyyy">
				<div class="control-group">
					<table class="table table-hover tab_left dataTable" id="risultatiRiconciliazioneErrori" summary="....">
							<thead>
								<tr>
									<th scope="col" style="text-align: center;">Descrizione</th>
									<th scope="col" style="text-align: center;">Tipo Operazione Documento</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
							<tfoot>
							</tfoot>
						</table>
						<s:hidden id="HIDDEN_startPosition_errori" name="startPosition" value="%{savedDisplayStart}" />
				</div>
			</fieldset>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-primary pull-right" data-dismiss="modal">
				Chiudi
			</button>
		</div>
	</div>

	<!-- SIAC-8046 CM 18/03-12/04/2021 Task 2.1/2.2 Inizio -->
	<!-- MODAL AGGIORNA ANNO E NUMERO ACCERTAMENTO-->
	<div id="aggiornaAccertamentoModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="infoAccertamento" aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3 id="infoAccertamento">Aggiorna anno e numero accertamento</h3>
		</div>
		<div class="modal-body">
			<div class="alert alert-error hide" id="ERRORI_ACCERTAMENTO_MODALE">
				<button type="button" class="close" data-hide="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul>
				</ul>
			</div>
			<fieldset class="form-horizontal" id="FIELDSET_modaleAccertamento">
				<div class="control-group">
					<label class="control-label" for="infoaccertamento">Anno/Numero Accertamento</label>
					<div class="controls">
						<s:textfield id="infoAccertamentoModale" cssClass="lbTextSmall span2" name="infoAccertamentoModale" disabled="true"></s:textfield> <span class="al">
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="annoaccertamentonuovo">Nuovo Anno Accertamento *</label>
					<div class="controls">
						<s:textfield id="annoaccertamentonuovo" maxlength="4" cssClass="lbTextSmall span2 soloNumeri" name="nAnnonuovo"></s:textfield> <span class="al">
						<label	class="radio inline" for="numeroaccertamentonuovo">Nuovo Numero Accertamento *</label>
						</span> 
						<s:textfield id="numeroaccertamentonuovo" cssClass="lbTextSmall span2 soloNumeri" name="nAccertamentonuovo"></s:textfield> <span class="al"> 
						</span>
					</div>

					<!-- SIAC-8046 CM 29/03-12/04/2021 Task 2.1/2.2 Inizio -->
					<br/>
					<div id="warning_componente" class="alert-warning">
						<strong>Attenzione!!</strong><br>
						<ul><li>I nuovi valori inseriti potrebbero non essere recepiti nella prossima elaborazione (se attualmente in corso), si dovrà attendere l’elaborazione successiva</li>
						</ul>
					</div>
					<!-- SIAC-8046 CM 29/03-12/04/2021 Task 2.1/2.2 Fine -->
				</div>
			</fieldset>
		</div>
		<!-- <input type="hidden" id="hidden_modificaEffettuataConSuccessoModaleAccertamento" value="" />
		<input type="hidden" id="hidden_accertamentoEsisteEValido" value="" /> -->
		<div class="modal-footer">	
		<!-- SIAC--8046 CM Task 2.2 30/03/2021 Inizio -->
			 <button type="button"  class="btn btn-primary" id="pulsanteAggiornaModaleAccertamento">salva</button> 
		<!-- SIAC--8046 CM Task 2.2 30/03/2021 Fine -->
		</div>
	</div>
	<!-- SIAC-8046 CM 18/03-12/04/2021 Task 2.1/2.2 Fine -->


		</div>
	</div>

	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/elaborazioniFlusso/consultaElaborazione.js"></script>	

</body>
</html>