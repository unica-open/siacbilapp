<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="guidaImpegnoLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleImpegno">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane" id="guidaImpegnoLabel">Seleziona impegno</h4>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_IMPEGNO_MODALE">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<fieldset class="form-horizontal" id="FIELDSET_modaleImpegno">
			<div class="control-group">
				<label for="annoImpegnoModale" class="control-label">Anno </label>
				<div class="controls">
					<input type="text" name="modaleImpegno.impegno.annoMovimento" class="lbTextSmall span2 soloNumeri" id="annoImpegnoModale"  maxlength="4">
					<span class="al">
						<label for="numeroImpegnoModale" class="radio inline">Numero </label>
					</span>
					<input type="text" name="modaleImpegno.impegno.numero" value="" class="lbTextSmall span2 soloNumeri trim" id="numeroImpegnoModale" >
				</div>
			</div>
			<%--************************************* Parte Aggiunta in data 14/01/2015 CR-2816 BEGIN ********************* --%>
			<div class="control-group">
				<label class="control-label" for="annoProvvedimento_modaleImpegno">Anno Provvedimento</label>
				<div class="controls">
					<s:textfield id="annoProvvedimento_modaleImpegno" cssClass="span2 soloNumeri" name="modaleImpegno.attoAmministrativo.anno" maxlength="4" />
					<span class="al">
						<label class="radio inline" for="numeroProvvedimento_modaleImpegno">Numero</label>
					</span>
					<s:textfield id="numeroProvvedimento_modaleImpegno" cssClass="span2 soloNumeri" name="modaleImpegno.attoAmministrativo.numero" maxlength="7" />
					<span class="al">
						<label class="radio inline" for="tipoAttoProvvedimento_modaleImpegno">Tipo</label>
					</span>
					<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="modaleImpegno.attoAmministrativo.tipoAtto.uid" id="tipoAttoProvvedimento_modaleImpegno" cssClass="lbTextSmall span3"
						headerKey="" headerValue="" />
					
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Struttura Amministrativa</label>
				<div class="controls">
					<div class="accordion span9 struttAmm" id="accordionPadreStrutturaAmministrativa_modale">
						<div class="accordion-group">
							<div class="accordion-heading">
								<a href="#struttAmm_modaleImpegno" data-toggle="collapse" class="accordion-toggle collapsed">
									<span id="SPAN_StrutturaAmministrativoContabile_modaleImpegno">Seleziona la Struttura amministrativa</span>
								</a>
							</div>
							<div id="struttAmm_modaleImpegno" class="accordion-body collapse">
								<div class="accordion-inner">
									<ul id="treeStruttAmm_modaleImpegno" class="ztree treeStruttAmm"></ul>
<!-- 								<button id ="pulsanteConfermaStruttAmm_modaleImpegno" type="button" class="btn btn-primary pull-right" data-toggle="collapse" data-target="#struttAmm_modaleImpegno">Conferma</button> -->
								</div>
							</div>
						</div>
					</div>

					<s:hidden id="HIDDEN_StrutturaAmministrativoContabile_modaleImpegnoUid" name="modaleImpegno.strutturaAmministrativoContabile.uid" />
					<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodice_modaleImpegno" name="modaleImpegno.strutturaAmministrativoContabile.codice" />
					<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione_modaleImpegno" name="modaleImpegno.strutturaAmministrativoContabile.descrizione" />
				</div>
				</div>
				<div class="control-group">
					<span class="al">
						<a class="btn btn-primary pull-right" id="pulsanteRicercaImpegnoModale">
							<i class="icon-search icon"></i>cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteRicercaImpegnoModale"></i>
						</a>
					</span>
				</div>
			
			<%--************************************* Parte Aggiunta in data 14/01/2015 CR-2816 FINE ********************* --%>
		</fieldset>
			<%-- ***************************************************************************************** --%>
		
		<div id="divListaImpegniTrovati" class="hide">
			<table class="table table-hover tab_left dataTable" id="risultatiRicercaImpegni" summary="....">
				<thead>
					<tr>
						<th scope="col"></th>
						<th scope="col">Impegno</th>
						<th scope="col">Capitolo</th>
						<th scope="col">Provvedimento</th>
						<th class="text_Right" scope="col">Importo</th>
						<th class="tab_Right" scope="col">Disponibile</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
				<tfoot>
				</tfoot>
			</table>
		</div>
		<%-- ***************************************************************************************** --%>
		<div id="divImpegniTrovati" class="hide">
			<h4>Elenco elementi trovati</h4>
			<table class="table table-hover tab_left" id="tabellaImpegnoModale">
					<thead>
						<tr>
							<th scope="col">Capitolo</th>
							<th scope="col">Provvedimento</th>
							<th scope="col">Soggetto</th>
							<th class="tab_Right" scope="col">Importo</th>
							<th class="tab_Right" scope="col">Disponibile</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td scope="col" id="tabellaImpegno_tdCapitolo"></td>
							<td scope="col" id="tabellaImpegno_tdProvvedimento"></td>
							<td scope="col" id="tabellaImpegno_tdSoggetto"></td>
							<td class="tab_Right whitespace-pre" scope="col" id="tabellaImpegno_tdImporto"></td>
							<td class="tab_Right" scope="col" id="tabellaImpegno_tdDisponibile"></td>
						</tr>
					</tbody>
				</table>
				<table class="table table-hover tab_left" id="tabellaImpegniModale">
					<thead>
						<tr>
							<th scope="col"></th>
							<th scope="col">Numero <abbr title="Subimpegno">Sub.</abbr></th>
							<th scope="col">Descrizione</th>
							<th scope="col">Soggetto</th>
							<th class="tab_Right" scope="col">Importo</th>
							<th class="tab_Right" scope="col">Disponibilit&agrave; a liquidare</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
				</div>
	</div>
	<input type="hidden" id="hidden_ricercaEffettuataConSuccessoModaleImpegno" value="" />
	<input type="hidden" id="hidden_disponibileImpegno" value="" />
	<input type="hidden" id="hidden_cigImpegno" value="" />
	<input type="hidden" id="hidden_cupImpegno" value="" />
	<input type="hidden" id="hidden_flagRilevanteIva" value="" />
	<%-- campo aggiunto per integrazione con servizio ricercaImpegniPerChiaveOttimizzato --%>
	<input type="hidden" id="hidden_caricaSub"  name="caricaSub" value="true" />
	<div class="modal-footer">
		<button class="btn btn-primary" id="pulsanteConfermaModaleImpegno">conferma</button>
	</div>
</div>