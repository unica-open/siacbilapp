<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<div id="editClassifGSA" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="row-fluid">
		<div class="overlay-modale">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h3 id="titoloModaleEditClassifGSA"></h3><%--Modifica Stanziamenti Capitolo/UEB xxxxxxxxxxx</h3>--%>
			</div>
			<div class="modal-body">
				<div class="alert alert-error alert-persistent hide conceal-on-show" id="ERRORI_modaleEditClassifGSA">
					<button type="button" class="close" data-hide="alert">&times;</button>
					<strong>Attenzione!!</strong><br>
						<ul>
						</ul>
				</div>
				<div class="alert alert-success hide conceal-on-show" id="INFORMAZIONI_modaleEditClassifGSA">
					<button type="button" class="close" data-hide="alert">&times;</button>
					<strong>Informazioni</strong><br>
					<ul>
					</ul>
				</div>
				
				<fieldset class="form-horizontal" id="fieldsetInserimentoAggiornamento">
					<h4 class="step-pane">Dati Classificatore</h4>
					<div class="control-group">
						<label class="control-label" for="codiceClassificatoreModal">Codice</label>
						<div class="controls">
							<input type="text" id="codiceClassificatoreModal" name="classificatoreGSA.codice" class="span6"/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="descrizioneClassificatoreModal">Descrizione </label>
						<div class="controls">
							<input type="text" id="descrizioneClassificatoreModal" name="classificatoreGSA.descrizione"/>
							<span class="alRight">
									<label for="statoOperativiClassificatoreGSAModal" class="radio inline">Stato </label>
								</span>
								<input id ="statoOperativiClassificatoreGSAModal" type="text" disabled="disabled" />
								<input id ="HIDDEN_statoOperativiClassificatoreGSAModal" name="descrizioneStatoOperativoClassificatore" type="hidden" value="" />
						</div>
					</div>
					<div class="clear"></div> 
				</fieldset>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">chiudi</button>
				<button  type = "button" class="btn btn-primary hide conceal-on-show" id="EDIT_inserisci">inserisci</button>
				<button  type = "button" class="btn btn-primary hide conceal-on-show" id="EDIT_aggiorna">aggiorna</button>
			</div>
		</div>
	</div>
</div>