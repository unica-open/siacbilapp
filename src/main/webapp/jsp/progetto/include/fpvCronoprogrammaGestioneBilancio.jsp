<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
	<%-- Modale FPV complessivo cronoprogramma dagestione dabilancio ahmad --%>
	<div aria-hidden="true" aria-labelledby="msgFPVcomplessivoDaGestioneDaBilancioLabel"
		role="dialog" tabindex="-1" class="modal hide fade"
		id="modaleFPVComplessivoDaGestioneDaBilancio">
		<div class="modal-header">
			<button aria-hidden="true" data-dismiss="modal" class="close"
				type="button">&times;</button>
			<h4 class="nostep-pane">FPV complessivo</h4>
		</div>
		<div class="modal-body">
			<fieldset class="form-horizontal">
				<table summary="...." id="tabellaFPVComplessivoDaGestioneDaBilancio"
					class="table tab_left table-hover">
					<thead>
						<tr>
							<th scope="col">&nbsp;</th>
							<th scope="col" class="tab_Right">Entrata </th>
							<th scope="col" class="tab_Right">FPV entrata</th>
							<th scope="col" class="tab_Right">Spese</th>
							<th scope="col" class="tab_Right">FPV spesa</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</fieldset>
		</div>
		<div class="modal-footer">
			<button aria-hidden="true" data-dismiss="modal"
				class="btn btn-primary">chiudi</button>
		</div>
	</div>
	<%-- end Modale FPV complessivo cronoprogramma da gestione da bilancio  --%>
	
	<%-- Modale FPV entrata cronoprogramma da gestione da bilancio  --%>
	<div aria-hidden="true" aria-labelledby="msgFPVentrataDaGestioneDaBilancioLabel"
		role="dialog" tabindex="-1" class="modal hide fade"
		id="modaleFPVEntrataDaGestioneDaBilancio">
		<div class="modal-header">
			<button aria-hidden="true" data-dismiss="modal" class="close"
				type="button">&times;</button>
			<h4 class="nostep-pane">FPV entrata</h4>
		</div>
		<div class="modal-body">
			<fieldset class="form-horizontal">
				<table summary="...." id="tabellaFPVEntrataDaGestioneDaBilancio"
					class="table tab_left table-hover">
					<thead>
						<tr>
							<th scope="col">&nbsp;</th>
							<th scope="col" class="tab_Right">Entrata </th>
							<th scope="col" class="tab_Right">FPV Entrata per spesa
								corrente</th>
							<th scope="col" class="tab_Right">FPV entrata per spesa
								c/capitale</th>
							<th scope="col" class="tab_Right">Totale</th>
							<th scope="col" class="tab_Right">FPV Entrata complessivo</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</fieldset>
		</div>
		<div class="modal-footer">
			<button aria-hidden="true" data-dismiss="modal"
				class="btn btn-primary">chiudi</button>
		</div>
	</div>
	<%-- end Modale FPV entrata cronoprogramma da gestione da bilancio--%>
	<%-- Modale FPV uscita cronoprogramma da gestione da bilancio--%>
	<div aria-hidden="true" aria-labelledby="msgFPVspesaDaGestioneDaBilancioLabel"
		role="dialog" tabindex="-1" class="modal hide fade"
		id="modaleFPVUscitaDaGestioneDaBilancio">
		<div class="modal-header">
			<button aria-hidden="true" data-dismiss="modal" class="close"
				type="button">&times;</button>
			<h4 class="nostep-pane">FPV spesa</h4>
		</div>
		<div class="modal-body">
			<fieldset class="form-horizontal">
				<table summary="...." id="tabellaFPVUscitaDaGestioneDaBilancio"
					class="table tab_left table-hover">
					<thead>
						<tr>
							<th scope="col">&nbsp;</th>
							<th scope="col" class="tab_Right">Missione</th>
							<th scope="col" class="tab_Right">Programma</th>
							<th scope="col" class="tab_Right">Titolo</th>
							<th scope="col" class="tab_Right">Spesa </th>
							<th scope="col" class="tab_Right">FPV spesa</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</fieldset>
		</div>
		<div class="modal-footer">
			<button aria-hidden="true" data-dismiss="modal"
				class="btn btn-primary">chiudi</button>
		</div>
	</div>
	<%-- end Modale FPV uscita cronoprogramma da gestione da bilancio--%>
	
	
	<%-- Modale consultazione cronoprogrmma gestione ahmad --%>
	<div aria-hidden="true"
		aria-labelledby="msgConsultaCronoDaGestioneLabel" role="dialog"
		tabindex="-1" class="modal hide fade"
		id="modaleConsultaCronoprogrammaDaGestione">
		<div class="modal-header">
			<button aria-hidden="true" data-dismiss="modal" class="close"
				type="button">&times;</button>
			<h4 class="nostep-pane">
				Prospetto riassuntivo cronoprogramma <span
					id="consultazioneCronoprogrammaDaGestioneCodice"></span>
			</h4>
		</div>
		<div class="modal-body">
			<fieldset class="form-horizontal">
				<table summary="...."
					id="consultazioneCronoprogrammaDaGestioneTabella"
					class="table tab_left">
					<thead>
						<tr>
							<th scope="col">Anno Competenza</th>
							<th scope="col" class="tab_Right">Totale spese</th>
							<th scope="col" class="tab_Right">Totale Entrate Vincolate</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</fieldset>
		</div>
		<div class="modal-footer">
			<button aria-hidden="true" data-dismiss="modal"
				class="btn btn-primary">chiudi</button>
		</div>
	</div>
	<%-- end Modale consultazione  cronoprogrmma gestione ahmad --%>