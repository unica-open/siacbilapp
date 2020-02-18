<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="msgAnnullaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleConsultazioneComponenti">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		<h3 id="titoloModaleVariazioneStanziamenti"></h3><%--Modifica Stanziamenti Capitolo/UEB xxxxxxxxxxx</h3>--%>
	</div>
	
	<div class="modal-body">
		<div  id="componentiCapitolo" class="hide">
			<h5>Componenti</h5>
			<div class="span11">
				<table class="table table-hover table-bordered table-condensed tab_left" id="tabellaStanziamentiTotaliPerComponenteModale">
					<thead>
						<tr>
							<th>&nbsp;</th>
							<th>&nbsp;</th>
							<th class="span3"><abbr title="componente da eliminare sul capitolo">Da eliminare sul capitolo</abbr></th>
							<th class="span3">Importo ${annoEsercizioInt}</th>
							<th class="span3">Importo ${annoEsercizioInt +1}</th>
							<th class="span3">Importo ${annoEsercizioInt +2}</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
		<div class="clear"></div>
		
		<fieldset class="stanziamentiCapitolo" class="hide">
		
			<h5>Stanziamenti</h5>
			<fieldset class="form-horizontal">
				<div class="control-group">
					<label class="control-label" for="competenzaVariazioneAnno0Modale">Competenza ${annoEsercizioInt}</label>
					<div class="controls">
						<s:textfield id="competenzaVariazioneAnno0Modale" disabled="true" cssClass="lbTextSmall span3 text-right decimale soloNumeri" name="competenzaVariazione" />
						<span class="al">
							<label class="radio inline" for="residuoVariazioneModale">&nbsp;&nbsp;&nbsp;Residuo ${annoEsercizioInt}</label>
						</span>
						<s:textfield id="residuoVariazioneModale" disabled="true" cssClass="lbTextSmall span3 text-right decimale soloNumeri" name="residuoVariazione" />
						<span class="al">
							<label class="radio inline" for="cassaVariazioneModale">Cassa ${annoEsercizioInt}</label>
						</span>
						<s:textfield id="cassaVariazioneModale" disabled="true" cssClass="lbTextSmall span3 text-right decimale soloNumeri" name="cassaVariazione" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="competenzaVariazioneAnno1Modale">Competenza ${annoEsercizioInt + 1}</label>
					<div class="controls">
						<s:textfield id="competenzaVariazioneAnno1Modale" disabled="true" cssClass="lbTextSmall span3 text-right decimale soloNumeri" name="competenzaVariazione1" />
						<span class="al">
							<label class="radio inline" for="competenzaVariazioneAnno2Modale">Competenza ${annoEsercizioInt + 2}</label>
						</span>
						<s:textfield id="competenzaVariazioneAnno2Modale" disabled="true" cssClass="lbTextSmall span3 text-right decimale soloNumeri" name="competenzaVariazione2" />
					</div>
				</div>
			</fieldset>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-secondary" data-dismiss="modal" aria-hidden="true">chiudi</button>
	</div>
</div>