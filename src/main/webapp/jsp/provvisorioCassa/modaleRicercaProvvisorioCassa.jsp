<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!-- PROVVISORIO -->
<div aria-hidden="true" aria-labelledby="cercaProvvisorioDiCassaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleRicercaProvvisorioDiCassa">
	<div class="modal-header" id="cercaProvvisorioDiCassaLabel">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane">Seleziona provvisorio</h4>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modale_ProvvisorioDiCassa">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<fieldset class="form-horizontal">
			<div class="accordion-body collapse in" id="campiProvvisorio">
				<input type="hidden" id="modale_hidden_tipoProvvisorioDiCassa" name="modale.tipoProvvisorioDiCassa" data-maintain />
				<div class="control-group">
					<label class="control-label">Anno</label>
					<div class="controls">
						<span class="al">
							<label class="radio inline" for="modale_annoDa">Da</label>
						</span>
						<input type="text" name="modale.annoDa" class="span2 soloNumeri" id="modale_annoDa" maxlength="4" />
						<span class="al">
							<label class="radio inline" for="modale_annoA">A</label>
						</span>
						<input type="text" name="modale.annoA" class="span2 soloNumeri" id="modale_annoA" maxlength="4" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">Numero</label>
					<div class="controls">
						<span class="al">
							<label class="radio inline" for="modale_numeroDa">Da</label>
						</span>
						<input type="text" name="modale.numeroDa" class="span2 soloNumeri" id="modale_numeroDa" maxlength="8" />
						<span class="al">
							<label class="radio inline" for="modale_numeroA">A</label>
						</span>
						<input type="text" name="modale.numeroA" class="span2 soloNumeri" id="modale_numeroA" maxlength="8" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">Data emissione</label>
					<div class="controls">
						<span class="al">
							<label class="radio inline" for="modale_dataEmissioneDa">Da</label>
						</span>
						<input type="text" name="modale.dataEmissioneDa" class="span2 datepicker" id="modale_dataEmissioneDa" maxlength="10" />
						<span class="al">
							<label class="radio inline" for="modale_dataEmissioneA">A</label>
						</span>
						<input type="text" name="modale.dataEmissioneA" class="span2 datepicker" id="modale_dataEmissioneA" maxlength="10" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">Importo</label>
					<div class="controls">
						<span class="al">
							<label class="radio inline" for="modale_importoDa">Da</label>
						</span>
						<input type="text" name="modale.importoDa" class="span2 soloNumeri decimale" id="modale_importoDa" />
						<span class="al">
							<label class="radio inline" for="modale_importoA">A</label>
						</span>
						<input type="text" name="modale.importoA" class="span2 soloNumeri decimale" id="modale_importoA" />
					</div>
				</div>
				<!-- SIAC-6352 -->
				
						<div class="control-group">
							<label class="control-label" for="distinta">Conto evidenza</label>
							<div class="controls">
								<s:select list="listaContoTesoreria" id="listacontoTesoreria" headerKey=""  
									headerValue="" name="modale.contoTesoreria.codice" cssClass="span4" 
									listKey="codice" listValue="codice + ' - ' + descrizione" />
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" for="DescCaus">Descrizione causale</label>
							<div class="controls">
								<s:textfield rows="2" cols="15" id="DescCaus" name="modale.descCausale" cssClass="span9" ></s:textfield>
							</div>
						</div>
						<!-- 
						<div class="control-group">
							<label class="control-label" for="CodSottoCaus">Sotto causale</label>
							<div class="controls">
								<s:textfield id="subCausale" name="modale.subCausale" cssClass="span9"></s:textfield>
							</div>
						</div>
		 				-->
						<div class="control-group">
							<label class="control-label" for="DescSogg">Descrizione soggetto</label>
							<div class="controls">
								<s:textfield rows="2" cols="15" id="DescSogg" name="modale.denominazioneSoggetto" cssClass="span9" ></s:textfield>
							</div>
						</div>	
				
				<!-- SIAC-6352 -->
				
			</div>
			<button type="button" id="modale_pulsanteRicercaProvvisorioCassa" class="btn btn-primary pull-right">
				<i class="icon-search icon"></i>&nbsp;cerca
			</button>
			<div id="modale_divElencoProvvisorioCassa" class="hide">
				<h4>Elenco provvisori anno <s:property value="annoEsercizioInt" /></h4>
				<table class="table tab_left table-hover" id="modale_tabellaProvvisorioCassa">
					<thead>
						<tr>
							<th>&nbsp;</th>
							<th>Anno</th>
							<th>Numero</th>
							<th>Data</th>
							<th>Causale</th>
							<th>Soggetto Creditore</th>
							<th class="tab_Right">Importo</th>
							<th class="tab_Right">Importo da regolarizzare</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button type="button" aria-hidden="true" data-dismiss="modal" class="btn btn-secondary">annulla</button>
		<button type="button" class="btn btn-primary" id="modale_pulsanteConfermaProvvisorioCassa">conferma</button>
	</div>
</div>
<!-- PROVVISORIO -->